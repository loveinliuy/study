/**
 * A loader for load the detector.js
 */
(function () {
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.async = true;
    var protocol = location.protocol === 'https' ? 'https' : 'http';
    script.src = protocol + '://172.18.32.134:8080/detector.js';
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(script, s);
})();
