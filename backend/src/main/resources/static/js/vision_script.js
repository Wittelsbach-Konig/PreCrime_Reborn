document.addEventListener('DOMContentLoaded', function() {
    var btnViewVideo = document.getElementsByClassName('but');
    var modal = document.getElementById('videoModal');
    var videoPlayer = document.getElementById('videoPlayer');

    for (var i = 0; i < btnViewVideo.length; i++) {
        btnViewVideo[i].addEventListener('click', function() {
            var videoUrl = this.getAttribute('data-video-url');
            videoPlayer.setAttribute('src', videoUrl);
            modal.style.display = 'block';
        });
    }

    var closeBtn = document.getElementsByClassName('close')[0];
    closeBtn.addEventListener('click', function() {
        videoPlayer.setAttribute('src', '');
        modal.style.display = 'none';
    });
});