(() => {
    'use strict';

    function getCsrfToken() {
        const match = document.cookie.match(/(?:^|; )XSRF-TOKEN=([^;]*)/);
        return match ? decodeURIComponent(match[1]) : null;
    }

    const grid = document.getElementById('characterGrid');
    if (!grid) {
        return;
    }

    grid.addEventListener('click', async (event) => {
        const button = event.target.closest('.btn-delete-character');
        if (!button) {
            return;
        }

        if (!confirm('Delete this character?')) {
            return;
        }

        const characterId = button.dataset.characterId;
        const response = await fetch(`/api/characters/${characterId}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'X-XSRF-TOKEN': getCsrfToken()
            }
        });

        if (response.status === 204) {
            document.getElementById(`character-card-${characterId}`)?.remove();
        } else {
            alert('Could not delete this character.');
        }
    });
})();