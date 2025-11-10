/**
 * Ultra Light Demo - Application JavaScript
 *
 * Application-specific functionality
 */

/**
 * Handle choice selection (called from HTML onclick)
 */
function selectChoice(languageId) {
    console.log('Choice selected:', languageId);

    // Send AJAX request to save selection
    UL.ajax('/ultra-light/api/select',
        { language: languageId },
        function(response) {
            console.log('Server response:', response);

            if (response.success) {
                // Navigate to step 2
                // In real framework: this would update components without page refresh
                // For simplicity, we do a full navigation here
                window.location.href = '/ultra-light/app?step=2';
            } else {
                alert('Error: ' + response.message);
            }
        },
        function(error) {
            alert('Network error: ' + error);
        }
    );
}

/**
 * Go back to step 1
 */
function goBack() {
    console.log('Going back to step 1');
    window.location.href = '/ultra-light/app';
}

/**
 * Initialize page
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log('Application initialized');

    // Add hover effects to choice cards
    let choiceCards = document.querySelectorAll('.choice-card');
    choiceCards.forEach(function(card) {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px) scale(1.02)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1)';
        });
    });

    // Log session info (for demo purposes)
    fetch('/ultra-light/api/select')
        .then(response => response.json())
        .then(data => {
            if (data.hasSelection) {
                console.log('Current selection:', data.selectedLanguage);
            } else {
                console.log('No selection yet');
            }
        })
        .catch(error => console.log('Could not fetch selection'));
});

console.log('Application JS loaded');