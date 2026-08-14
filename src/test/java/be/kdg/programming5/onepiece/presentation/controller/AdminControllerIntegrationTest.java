package be.kdg.programming5.onepiece.presentation.controller;

import be.kdg.programming5.onepiece.business.service.CsvImportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CsvImportService csvImportService;

    @Test
    void showUploadForm_anonymousUser_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/admin/upload"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void showUploadForm_nonAdminUser_isForbidden() throws Exception {
        mockMvc.perform(get("/admin/upload"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void showUploadForm_adminUser_returnsForm() throws Exception {
        mockMvc.perform(get("/admin/upload"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/uploadCharacters"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void uploadCharacters_validCsv_dispatchesImportAndRedirectsWithoutWaitingForIt() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "characters.csv", "text/csv",
                ("name,age,appearance,powertype,power,crewName,swordName\n"
                        + "Jinbe,45,https://img,WILL,8.0,,\n").getBytes());

        mockMvc.perform(multipart("/admin/upload").file(file).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/upload"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(csvImportService).importCharacters(List.of("Jinbe,45,https://img,WILL,8.0,,"), "admin");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void uploadCharacters_emptyFile_showsErrorAndNeverDispatchesImport() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "empty.csv", "text/csv", new byte[0]);

        mockMvc.perform(multipart("/admin/upload").file(file).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("errorMessage"));

        verifyNoInteractions(csvImportService);
    }
}
