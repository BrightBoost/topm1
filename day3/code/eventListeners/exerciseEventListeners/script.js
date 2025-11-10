let demoSwitch = document.getElementById('demo-switch');
let demoBulb = document.getElementById('demo-bulb');
let demoTitle = document.getElementById('demo-title');
let demoApp = document.getElementById('demo-app');
let demoIsOn = false;

demoSwitch.addEventListener('click', function () {
    demoIsOn = !demoIsOn;

    if (demoIsOn) {
        demoBulb.classList.add('on');
        demoSwitch.classList.add('on');
        demoApp.classList.add('dark');
        demoTitle.textContent = 'Lamp is AAN';
        demoTitle.style.color = 'white';
    } else {
        demoBulb.classList.remove('on');
        demoSwitch.classList.remove('on');
        demoApp.classList.remove('dark');
        demoTitle.textContent = 'Lamp is UIT';
        demoTitle.style.color = '#666';
    }
});

demoSwitch.addEventListener('mouseenter', function () {
    demoSwitch.style.transform = 'scale(1.1)';
});

demoSwitch.addEventListener('mouseleave', function () {
    demoSwitch.style.transform = 'scale(1)';
});
