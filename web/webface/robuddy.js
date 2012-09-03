(function(){
	var robuddy = {
		connect: function () {
			if( this.connTimer != undefined) {
				return;
			}
			if( this.conn != undefined && this.conn.readyState == 1)
				return;
			var connection = new WebSocket('ws://' + document.location.host + "/chat");
			if( this._connL != undefined)
				this._connL.onconnecting();
			connection.onopen    = function(){window.robuddy.onconnect()};
			connection.onclose   = function(){window.robuddy.onclose()};
			connection.onerror   = function(e){window.robuddy.onerror(e)};
			connection.onmessage = function(e){window.robuddy.onmessage(e)};
		    this.conn = connection;
		},
		
		reconnect: function () {
			if( this.connTimer != undefined) {
				window.clearTimeout( this.connTimer);
				delete this.connTimer;				
			}
			this.connect();
		},
		
		subscribe: function (channel) {
			
		},
		
		publish: function(event) {
			if( this.conn != undefined) {
				this.conn.send( "p:" + JSON.stringify(event));
			}
		},

		start: function ( connListener) {
			if( connListener != undefined) {
				this._connL = connListener;
			}
			this.connect();
		},
		
		onconnect: function () {
			if( this._connL != undefined)
				this._connL.onconnect();
		},
		
		onmessage: function (event) {
			
		},
		
		onclose: function () {
			if( this._connL != undefined)
				this._connL.onclose();
			var timer = setTimeout( function(){window.robuddy.reconnect()}, 10000);
			this.connTimer = timer;
		},
		
		onerror: function (error) {
			if( this._connL != undefined && this._connL.onerror!=undefined)
				this._connL.onerror(error);
		},
	}
		
	robuddy.MxEvent = function(channel, signal, data) {
		this.channel = channel;
		this.signal = signal;
		if( data != undefined)
			this.data = data;
		this.publish = function () { window.robuddy.publish(this)};
		return this;
		}
			
	var MxChannel = {
		EVENT_CH_PRIVATE: 0,
		EVENT_CH_RESERVED: 1,
		EVENT_CH_ACC_S: 2,
		EVENT_CH_ACC_C: 3,
		EVENT_CH_MOVE_S: 4,
		EVENT_CH_MOVE_C: 5,
		EVENT_CH_RANGE_S: 6,
		EVENT_CH_RANGE_C: 7,
		//
		MAX_ROBODY_PUB_EVENT_CH: 256,
	}
	
	var MxSignal = {
		MX_SIG_EMPTY: 0,		///< signal for trace superstate	
		MX_SIG_ENTRY: 1,		///< signal for entry actions
    	MX_SIG_EXIT: 2,			///< signal for exit actions
    	MX_SIG_INIT: 3,			///< signal for nested initial transitions
    	MX_SIG_INFO: 4,
    	MX_SIG_OOB: 5,
    	EVENT_SIG_RESET: 6, /*= Q_USER_SIG*/
		EVENT_SIG_TIMEOUT: 7,
		//
		MAX_BASIC_EVENT_SIG: 32,
	}
	
	var MoveSignal = {
		MV_SIG_RAW: 32,
		MV_SIG_STEER: 33,
		MV_SIG_ACCEL: 34,
		MV_SIG_BRAKE: 35,
		//
		MAX_MOVE_SIG: 36,
	}
	
	robuddy.MoveRawEvent = function ( leftSpeed, rightSpeed) {
		robuddy.MxEvent.call(this, MxChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_RAW);
		this.leftSpeed = leftSpeed;
		this.rightSpeed = rightSpeed;
		return this;
	}
	
	robuddy.MoveSteerEvent = function ( direction) {
		robuddy.MxEvent.call(this, MxChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_STEER);
		this.direction = direction;
		return this;
	}
	
	robuddy.MoveAccelEvent = function ( speed) {
		robuddy.MxEvent.call(this, MxChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_ACCEL);
		this.speed = speed;
		return this;
	}
	
	robuddy.MoveBrakeEvent = function ( level) {
		robuddy.MxEvent.call(this, MxChannel.EVENT_CH_MOVE_C, MoveSignal.MV_SIG_BRAKE);
		this.level = level;
		return this;
	}
	
	window.robuddy = robuddy;

})(window);
