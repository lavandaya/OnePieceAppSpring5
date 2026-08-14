import trashIconSvg from "bootstrap-icons/icons/trash3-fill.svg";
import Swal from "sweetalert2";

function getCsrfToken() {
    const match = document.cookie.match(/(?:^|; )XSRF-TOKEN=([^;]*)/);
    return match ? decodeURIComponent(match[1]) : null;
}

export function initCharacterDelete() {
    const grid = document.getElementById("characterGrid");
    if (!grid) {
        return;
    }

    grid.addEventListener("click", async (event) => {
        const button = event.target.closest(".btn-delete-character");
        if (!button) {
            return;
        }

        const result = await Swal.fire({
            iconHtml: trashIconSvg,
            title: "Delete this character?",
            text: "This cannot be undone.",
            showCancelButton: true,
            confirmButtonText: "Delete",
            confirmButtonColor: "#d62828",
        });

        if (!result.isConfirmed) {
            return;
        }

        const characterId = button.dataset.characterId;
        const response = await fetch(`/api/characters/${characterId}`, {
            method: "DELETE",
            headers: {
                "Accept": "application/json",
                "X-XSRF-TOKEN": getCsrfToken(),
            },
        });

        if (response.status === 204) {
            document.getElementById(`character-card-${characterId}`)?.remove();
        } else {
            await Swal.fire({ icon: "error", title: "Could not delete this character." });
        }
    });
}
