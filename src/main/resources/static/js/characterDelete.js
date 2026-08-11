(() => {
    'use strict';

    document.querySelectorAll('.btn-delete-character').forEach((button) => {
        button.addEventListener('click', async () => {
            if (!confirm('Delete this character?')) {
                return;
            }

            const characterId = button.dataset.characterId;
            const response = await fetch(`/api/characters/${characterId}`, {
                method: 'DELETE'
            });

            if (response.status === 204) {
                document.getElementById(`character-card-${characterId}`)?.remove();
            } else {
                alert('Could not delete this character.');
            }
        });
    });
})();