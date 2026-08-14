import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime.js";

dayjs.extend(relativeTime);

function formatBattleDate(isoString) {
    const date = dayjs(isoString);
    return `${date.format("YYYY-MM-DD HH:mm")} (${date.fromNow()})`;
}

export function initCharacterBattles() {
    const reloadButton = document.getElementById("reloadBattlesButton");
    if (!reloadButton) {
        return;
    }

    const characterId = reloadButton.dataset.characterId;
    const tableBody = document.getElementById("battlesTableBody");
    const emptyAlert = document.getElementById("battlesEmptyAlert");
    const battlesTable = document.getElementById("battlesTable");

    reloadButton.addEventListener("click", async () => {
        const response = await fetch(`/api/characters/${characterId}/battles`, {
            headers: { "Accept": "application/json" },
        });

        if (!response.ok) {
            alert("Could not load battles.");
            return;
        }

        const battles = await response.json();
        tableBody.innerHTML = "";

        if (battles.length === 0) {
            emptyAlert.classList.remove("d-none");
            battlesTable.classList.add("d-none");
            return;
        }

        emptyAlert.classList.add("d-none");
        battlesTable.classList.remove("d-none");

        battles.forEach((battle) => {
            const row = document.createElement("tr");

            const nameCell = document.createElement("td");
            const nameLink = document.createElement("a");
            nameLink.href = `/battles/${battle.id}`;
            nameLink.textContent = battle.name;
            nameCell.appendChild(nameLink);

            const locationCell = document.createElement("td");
            locationCell.textContent = battle.location;

            const dateCell = document.createElement("td");
            dateCell.textContent = formatBattleDate(battle.date);

            const winnerCell = document.createElement("td");
            winnerCell.textContent = battle.winner;

            row.append(nameCell, locationCell, dateCell, winnerCell);
            tableBody.appendChild(row);
        });
    });
}
