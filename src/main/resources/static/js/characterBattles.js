(() => {
    'use strict';

    const reloadButton = document.getElementById('reloadBattlesButton');
    if (!reloadButton) {
        return;
    }

    const characterId = reloadButton.dataset.characterId;
    const tableBody = document.getElementById('battlesTableBody');
    const emptyAlert = document.getElementById('battlesEmptyAlert');
    const battlesTable = document.getElementById('battlesTable');

    function formatDate(isoString) {
        return new Date(isoString).toISOString().slice(0, 16).replace('T', ' ');
    }

    reloadButton.addEventListener('click', async () => {
        const response = await fetch(`/api/characters/${characterId}/battles`, {
            headers: { 'Accept': 'application/json' }
        });

        if (!response.ok) {
            alert('Could not load battles.');
            return;
        }

        const battles = await response.json();
        tableBody.innerHTML = '';

        if (battles.length === 0) {
            emptyAlert.classList.remove('d-none');
            battlesTable.classList.add('d-none');
            return;
        }

        emptyAlert.classList.add('d-none');
        battlesTable.classList.remove('d-none');

        battles.forEach((battle) => {
            const row = document.createElement('tr');

            const nameCell = document.createElement('td');
            const nameLink = document.createElement('a');
            nameLink.href = `/battles/${battle.id}`;
            nameLink.textContent = battle.name;
            nameCell.appendChild(nameLink);

            const locationCell = document.createElement('td');
            locationCell.textContent = battle.location;

            const dateCell = document.createElement('td');
            dateCell.textContent = formatDate(battle.date);

            const winnerCell = document.createElement('td');
            winnerCell.textContent = battle.winner;

            row.append(nameCell, locationCell, dateCell, winnerCell);
            tableBody.appendChild(row);
        });
    });
})();