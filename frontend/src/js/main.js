import "../scss/main.scss";
import "sweetalert2/dist/sweetalert2.min.css";

import { initCharacterAdd } from "./modules/characterAdd.js";
import { initCharacterBattles } from "./modules/characterBattles.js";
import { initCharacterDelete } from "./modules/characterDelete.js";
import { initCharacterPatch } from "./modules/characterPatch.js";
import { initCharacterSearch } from "./modules/characterSearch.js";
import { initBootstrapValidation } from "./modules/validation.js";

document.addEventListener("DOMContentLoaded", () => {
    initBootstrapValidation();
    initCharacterAdd();
    initCharacterDelete();
    initCharacterPatch();
    initCharacterSearch();
    initCharacterBattles();
});
