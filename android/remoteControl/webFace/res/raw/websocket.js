function onLeft(){
		window.send('50 0');
}

function onRight(){
		window.send('0 50');
}

function onUp(){
		window.send('100 100');
}

function onDown(){
		window.send('-100 -100');
}

function onStop(){
		window.send('0 0');
}

function getKey() {
       	var e =window.event;
        var keycode =  e.keyCode;
        switch (keycode) {
	        case 37: //left
	        	onLeft();
	        	break;
	        case 38: //up
	        	onUp();
	        	break;
	        case 39: //right
	        	onRight();
	        	break;
	        case 40: //down
	        	onDown();
	        	break;
	        case 32: //stop
	         	onStop();
	         	break;
        }
}

window.onload = function() {
    ws = new WebSocket('ws://' + document.location.host + "/chat");
    ws.onopen    = function(e) { console.log('OPEN'); };
    ws.onclose   = function(e) { console.log('CLOSE'); };
    ws.onerror   = function(e) { console.log('ERROR'); };
    ws.onmessage = function(e) {
        var robotsStatus = document.getElementById('robotsStatus');
        robotsStatus.value += e.data + '\n';
        robotsStatus.scrollTop = robotsStatus.scrollHeight;
    };

    window.send = function (message) {
        ws.send(message);
    }
    
    document.onkeypress = getKey; //bind key press listener
	document.onkeydown = getKey; //bind key press listener
	/*
	 turnLeft = document.getElementById('turnLeft');
	
	 turnRight = document.getElementById('turnRight');
	 turnUp = document.getElementById('turnUp');
	 turnDown = document.getElementById('turnDown');
	
	turnLeft.onclik = function(e) {
	    alert("left");
		send('left');
	};
	
	turnRight.onclik = function(e) {
		send('right');
	};
	
	turnUp.onclik = function(e) {
		send('up');
	};
	
	turnDown.onclik = function(e) {
		send('down');
	};
	*/
}

