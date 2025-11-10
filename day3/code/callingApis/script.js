// Demo code - Load User
document.getElementById('load-user-btn').addEventListener('click', function () {
    let output = document.getElementById('output');
    let loading = document.getElementById('loading');

    // Show loading
    loading.classList.add('show');
    output.textContent = '';

    // Create XMLHttpRequest
    let xhr = new XMLHttpRequest();

    // Setup callback
    xhr.onreadystatechange = function () {
        console.log('readyState:', xhr.readyState, 'status:', xhr.status);

        if (xhr.readyState === 4 && xhr.status === 200) {
            // Hide loading
            loading.classList.remove('show');

            // Parse JSON
            let user = JSON.parse(xhr.responseText);

            // Display data
            output.textContent = 'User data loaded!\n\n' +
                'Name: ' + user.name + '\n' +
                'Username: ' + user.username + '\n' +
                'Email: ' + user.email + '\n' +
                'Phone: ' + user.phone + '\n' +
                'City: ' + user.address.city + '\n' +
                'Company: ' + user.company.name;

            console.log('User data:', user);
        }
    };

    // Open connection
    xhr.open('GET', 'https://jsonplaceholder.typicode.com/users/1', true);

    // Send request
    xhr.send();
    console.log('Request sent');
});

// Demo code - Load Posts
document.getElementById('load-posts-btn').addEventListener('click', function () {
    let output = document.getElementById('output');
    let loading = document.getElementById('loading');

    loading.classList.add('show');
    output.textContent = '';

    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            loading.classList.remove('show');

            let posts = JSON.parse(xhr.responseText);

            // Display first 3 posts
            let text = 'Posts loaded!\n\n';
            posts.slice(0, 3).forEach(function (post, index) {
                text += '--- Post ' + (index + 1) + ' ---\n';
                text += 'Title: ' + post.title + '\n';
                text += 'Body: ' + post.body + '\n\n';
            });

            output.textContent = text;
            console.log('Posts:', posts);
        }
    };

    xhr.open('GET', 'https://jsonplaceholder.typicode.com/posts', true);
    xhr.send();
});
