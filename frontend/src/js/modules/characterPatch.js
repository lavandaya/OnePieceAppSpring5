function getCsrfToken() {
    const match = document.cookie.match(/(?:^|; )XSRF-TOKEN=([^;]*)/);
    return match ? decodeURIComponent(match[1]) : null;
}

export function initCharacterPatch() {
    const panel = document.getElementById("patchCharacterPanel");
    if (!panel) {
        return;
    }

    const characterId = panel.dataset.characterId;
    const form = document.getElementById("patchCharacterForm");
    const alertBox = document.getElementById("patchAlert");
    const powerBadge = document.getElementById("characterPowerBadge");
    const swordBadge = document.getElementById("characterSwordBadge");
    const submitButton = form.querySelector("button[type=\"submit\"]");

    const initialValues = new Map();
    form.querySelectorAll("[data-field]").forEach((input) => initialValues.set(input, input.value));

    function refreshSubmitState() {
        const changed = [...initialValues.entries()].some(([input, value]) =>
            input.value !== value
        );
        submitButton.disabled = !changed;
    }

    form.querySelectorAll("[data-field]").forEach((input) => {
        input.addEventListener("input", refreshSubmitState);
    });
    refreshSubmitState();

    function clearFeedback() {
        form.querySelectorAll("[data-field]").forEach((input) =>
            input.classList.remove("is-invalid")
        );
        form.querySelectorAll("[data-error-for]").forEach((box) => box.textContent = "");
        alertBox.classList.add("d-none");
        alertBox.className = "alert d-none";
        alertBox.textContent = "";
    }

    function showAlert(message, variant) {
        alertBox.className = `alert alert-${variant}`;
        alertBox.textContent = message;
    }

    function showFieldErrors(errors) {
        Object.entries(errors).forEach(([field, message]) => {
            form.querySelector(`[data-field="${field}"]`)?.classList.add("is-invalid");
            const box = form.querySelector(`[data-error-for="${field}"]`);
            if (box) {
                box.textContent = message;
            }
        });
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearFeedback();

        const payload = {};
        form.querySelectorAll("[data-field]").forEach((input) => {
            const raw = input.value.trim();
            if (raw === "" || raw === initialValues.get(input)) {
                return;
            }
            payload[input.dataset.field] = input.type === "number" ? Number(raw) : raw;
        });

        if (Object.keys(payload).length === 0) {
            return;
        }

        const response = await fetch(`/api/characters/${characterId}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                "X-XSRF-TOKEN": getCsrfToken(),
            },
            body: JSON.stringify(payload),
        });

        if (response.status === 200) {
            const updated = await response.json();
            powerBadge.textContent = `${updated.power} DON`;
            if (swordBadge && updated.swordName) {
                swordBadge.textContent = updated.swordName;
            }
            form.querySelectorAll("[data-field]").forEach((input) =>
                initialValues.set(input, input.value)
            );
            refreshSubmitState();
            showAlert("Changes saved.", "success");
            return;
        }

        const body = await response.json();

        if (response.status === 400) {
            if (body.message) {
                showAlert(body.message, "danger");
            } else {
                showFieldErrors(body);
            }
            return;
        }

        showAlert(
            body.message ?? body.error ?? `Unexpected response: ${response.status}`,
            "danger",
        );
    });
}
