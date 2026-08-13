(() => {
    'use strict';
    const isAuthenticated =
        document.querySelector('meta[name="authenticated"]')?.content === 'true';
    const nameForm = document.getElementById('searchByNameForm');
    const powerForm = document.getElementById('searchByPowerForm');
    const resultsSection = document.getElementById('ajaxResultsSection');
    const resultsBody = document.getElementById('ajaxResultsBody');
    const resultsCount = document.getElementById('ajaxResultsCount');
    const noResultsAlert = document.getElementById('ajaxNoResults');
    const resultsTable = document.getElementById('ajaxResultsTable');

    async function searchCharacters(params) {
        const url = new URL('/api/characters', window.location.origin);
        Object.entries(params).forEach(([key, value]) => {
            if (value !== null && value !== '') {
                url.searchParams.set(key, value);
            }
        });

        const response = await fetch(url, {
            headers: { 'Accept': 'application/json' }
        });

        if (!response.ok) {
            throw new Error(`Search failed with status ${response.status}`);
        }

        return response.json();
    }

    function createCell(text) {
        const td = document.createElement('td');
        td.textContent = text;
        return td;
    }

    function renderResults(characters) {
        resultsSection.classList.remove('d-none');
        resultsCount.textContent = characters.length;

        if (characters.length === 0) {
            noResultsAlert.classList.remove('d-none');
            resultsTable.classList.add('d-none');
            return;
        }

        noResultsAlert.classList.add('d-none');
        resultsTable.classList.remove('d-none');
        resultsBody.innerHTML = '';

        characters.forEach((character) => {
            const row = document.createElement('tr');
            if (character.power >= 9.0) {
                row.classList.add('table-warning');
            }

            const nameCell = document.createElement('td');
            const nameLink = document.createElement('a');
            nameLink.href = `/characters/${character.id}`;
            nameLink.textContent = character.name;
            nameCell.appendChild(nameLink);

            const powertypeCell = document.createElement('td');
            const powertypeBadge = document.createElement('span');
            powertypeBadge.className = 'badge text-bg-info';
            powertypeBadge.textContent = character.powertype;
            powertypeCell.appendChild(powertypeBadge);

            const powerCell = document.createElement('td');
            const powerBadge = document.createElement('span');
            if (isAuthenticated) {
                powerBadge.className = 'badge text-bg-primary';
                powerBadge.textContent = `${character.power} DON`;
            } else {
                powerBadge.className = 'badge text-bg-secondary';
                powerBadge.textContent = '???';
            }
            powerCell.appendChild(powerBadge);

            row.append(createCell(character.id), nameCell, powertypeCell, powerCell);
            resultsBody.appendChild(row);
        });
    }

    if (nameForm) {
        nameForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const name = nameForm.querySelector('input[name="name"]').value;
            renderResults(await searchCharacters({ name }));
        });
    }

    if (powerForm) {
        powerForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const minPower = powerForm.querySelector('input[name="minPower"]').value;
            renderResults(await searchCharacters({ minPower }));
        });
    }
})();