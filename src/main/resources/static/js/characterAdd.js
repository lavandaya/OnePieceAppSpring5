(() => {
    'use strict';

    function getCsrfToken() {
        const match = document.cookie.match(/(?:^|; )XSRF-TOKEN=([^;]*)/);
        return match ? decodeURIComponent(match[1]) : null;
    }

    const form = document.getElementById('addCharacterForm');
    if (!form) {
        return;
    }

    const grid = document.getElementById('characterGrid');
    const alertBox = document.getElementById('addCharacterAlert');

    function fieldValue(field) {
        const input = form.querySelector(`[data-field="${field}"]`);
        const raw = input.value.trim();
        return raw === '' ? null : raw;
    }

    function numberValue(field) {
        const raw = fieldValue(field);
        return raw === null ? null : Number(raw);
    }

    function clearFeedback() {
        form.querySelectorAll('[data-field]').forEach((input) => input.classList.remove('is-invalid'));
        form.querySelectorAll('[data-error-for]').forEach((box) => box.textContent = '');
        alertBox.classList.add('d-none');
        alertBox.textContent = '';
    }

    function showFieldErrors(errors) {
        Object.entries(errors).forEach(([field, message]) => {
            form.querySelector(`[data-field="${field}"]`)?.classList.add('is-invalid');
            const box = form.querySelector(`[data-error-for="${field}"]`);
            if (box) {
                box.textContent = message;
            }
        });
    }

    function showAlert(message) {
        alertBox.textContent = message;
        alertBox.classList.remove('d-none');
    }

    function createCard(character) {
        const col = document.createElement('div');
        col.className = 'col';
        col.id = `character-card-${character.id}`;

        const card = document.createElement('div');
        card.className = 'card h-100 shadow-sm';
        if (character.power >= 9.0) {
            card.classList.add('border-warning', 'border-2');
        }

        const image = document.createElement('img');
        image.className = 'card-img-top character-img';
        image.src = character.appearance;
        image.alt = character.name;

        const body = document.createElement('div');
        body.className = 'card-body';

        const title = document.createElement('h5');
        title.className = 'card-title d-flex justify-content-between align-items-center';

        const titleLeft = document.createElement('span');
        const nameSpan = document.createElement('span');
        nameSpan.textContent = character.name;
        titleLeft.appendChild(nameSpan);

        if (character.swordName) {
            const swordBadge = document.createElement('span');
            swordBadge.className = 'badge text-bg-warning ms-1';
            swordBadge.textContent = 'Swordsman';
            titleLeft.appendChild(swordBadge);
        }
        title.appendChild(titleLeft);

        const text = document.createElement('p');
        text.className = 'card-text';

        const powerBadge = document.createElement('span');
        powerBadge.className = 'badge text-bg-primary';
        powerBadge.textContent = `${character.power} DON`;

        const typeBadge = document.createElement('span');
        typeBadge.className = 'badge text-bg-info';
        typeBadge.textContent = character.powertype;

        text.append(powerBadge, ' ', typeBadge);
        body.append(title, text);

        const footer = document.createElement('div');
        footer.className = 'card-footer bg-transparent border-top-0 d-flex gap-2';

        const detailLink = document.createElement('a');
        detailLink.className = 'btn btn-sm btn-outline-primary flex-grow-1';
        detailLink.href = `/characters/${character.id}`;
        detailLink.textContent = 'View details';

        const deleteButton = document.createElement('button');
        deleteButton.type = 'button';
        deleteButton.className = 'btn btn-sm btn-outline-danger btn-delete-character';
        deleteButton.dataset.characterId = character.id;
        deleteButton.title = 'Delete';
        const trashIcon = document.createElement('i');
        trashIcon.className = 'bi bi-trash';
        deleteButton.appendChild(trashIcon);

        footer.append(detailLink, deleteButton);
        card.append(image, body, footer);
        col.appendChild(card);
        return col;
    }

    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        clearFeedback();

        const payload = {
            name: fieldValue('name'),
            age: numberValue('age'),
            appearance: fieldValue('appearance'),
            powertype: fieldValue('powertype'),
            power: numberValue('power'),
            crewName: fieldValue('crewName'),
            swordName: fieldValue('swordName')
        };

        const response = await fetch('/api/characters', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'X-XSRF-TOKEN': getCsrfToken()
            },
            body: JSON.stringify(payload)
        });

        if (response.status === 201) {
            const created = await response.json();
            grid.appendChild(createCard(created));
            form.reset();
            return;
        }

        if (response.status === 400) {
            const body = await response.json();
            if (body.message) {
                showAlert(body.message);
            } else {
                showFieldErrors(body);
            }
            return;
        }

        showAlert(`Unexpected response: ${response.status}`);
    });
})();