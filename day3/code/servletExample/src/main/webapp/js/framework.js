/**
 * Wicket Ultra Light - Framework JavaScript
 *
 * Core functionality for the framework:
 * - AJAX communication
 * - Component updates
 * - State management
 */

// Framework namespace
const FrameworkUltraLight = {

    /**
     * Make an AJAX POST request
     */
    ajax: function(url, data, onSuccess, onError) {
        // Show loading
        this.showLoading();

        // Convert data to FormData
        let formData = new FormData();
        for (let key in data) {
            formData.append(key, data[key]);
        }

        // Send request
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'  // JSON!
            },
            body: JSON.stringify(data)  // Convert to JSON string
        })
        .then(response => response.json())
        .then(data => {
            this.hideLoading();
            if (onSuccess) onSuccess(data);
        })
        .catch(error => {
            this.hideLoading();
            console.error('AJAX error:', error);
            if (onError) onError(error);
        });
    },

    /**
     * Update a component via AJAX
     * This is similar to how Wicket's AjaxRequestTarget works!
     */
    updateComponent: function(componentId, onSuccess) {
        console.log('Updating component:', componentId);
        // In a real framework, this would fetch new HTML for the component
        // For this demo, we just navigate to the new page
        if (onSuccess) onSuccess();
    },

    /**
     * Navigate to a page (with optional AJAX)
     */
    navigateTo: function(url, ajax = false) {
        if (ajax) {
            // AJAX navigation - update content without page refresh
            console.log('AJAX navigate to:', url);
            window.history.pushState({}, '', url);
            // In real Wicket: fetch new content and replace container
            window.location.href = url;
        } else {
            window.location.href = url;
        }
    },

    /**
     * Show loading indicator
     */
    showLoading: function() {
        let loading = document.getElementById('loading');
        if (loading) {
            loading.style.display = 'block';
        }
    },

    /**
     * Hide loading indicator
     */
    hideLoading: function() {
        let loading = document.getElementById('loading');
        if (loading) {
            loading.style.display = 'none';
        }
    },

    /**
     * Get context path
     */
    getContextPath: function() {
        return window.location.pathname.split('/')[1];
    }
};

// Shorthand
const UL = FrameworkUltraLight;

// Log framework loaded
console.log('Framework Ultra Light framework loaded');
