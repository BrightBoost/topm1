// Demo 1: Simple Button
        document.getElementById('simple-button').addEventListener('click', function() {
            let output = document.getElementById('click-output');
            output.textContent = '✅ Button geklikt op ' + new Date().toLocaleTimeString();
            output.style.display = 'block';
            console.log('Simple button clicked!');
        });

        // Demo 2: Counter
        let count = 0;
        let counterDisplay = document.getElementById('counter');

        document.getElementById('increment-btn').addEventListener('click', function() {
            count++;
            counterDisplay.textContent = count;
            console.log('Count:', count);
        });

        document.getElementById('decrement-btn').addEventListener('click', function() {
            count--;
            counterDisplay.textContent = count;
            console.log('Count:', count);
        });

        document.getElementById('reset-btn').addEventListener('click', function() {
            count = 0;
            counterDisplay.textContent = count;
            console.log('Counter reset');
        });

        // Demo 3: Input Events
        let nameInput = document.getElementById('name-input');
        let greetingOutput = document.getElementById('greeting-output');

        nameInput.addEventListener('input', function(event) {
            let name = event.target.value;
            greetingOutput.textContent = 'Hallo, ' + (name || '...');
            console.log('Input changed:', name);
        });

        // Demo 4: Mouse Events
        let hoverBox = document.getElementById('hover-box');
        let mouseOutput = document.getElementById('mouse-output');

        hoverBox.addEventListener('mouseenter', function() {
            hoverBox.style.background = '#2ecc71';
            hoverBox.textContent = 'Je bent er!';
            mouseOutput.textContent = '🖱️ Mouse entered!';
        });

        hoverBox.addEventListener('mouseleave', function() {
            hoverBox.style.background = '#3498db';
            hoverBox.textContent = 'Hover over mij!';
            mouseOutput.textContent = '👋 Mouse left!';
        });

        hoverBox.addEventListener('click', function() {
            hoverBox.style.background = '#e74c3c';
            hoverBox.textContent = 'Geklikt!';
            mouseOutput.textContent = '🎯 Clicked!';
        });

        // Demo 5: Form Submit
        let goodForm = document.getElementById('good-form');
        
        goodForm.addEventListener('submit', function(event) {
            event.preventDefault();
            
            let name = document.getElementById('form-name').value;
            let email = document.getElementById('form-email').value;
            
            let output = document.getElementById('form-output');
            output.textContent = '✅ Form submitted!\nNaam: ' + name + '\nEmail: ' + email;
            output.style.display = 'block';
            
            console.log('Form data:', {name, email});
        });

        // Console logs
        console.log('✅ Demo 2 pagina is geladen!');
        console.log('💡 TIP: Klik op de knoppen en zie de events in actie');
        console.log('⚠️  Let op: scroll naar "Form Submit" om preventDefault() te zien');