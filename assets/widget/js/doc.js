function addEvent(elm, evType, fn, useCapture) {
	if (elm.addEventListener) {
		elm.addEventListener(evType, fn, useCapture);//DOM2.0
		return true;
	}
	else if (elm.attachEvent) {
			var r = elm.attachEvent('on' + evType, fn);//IE5+
			return r;
	}
	else {
			elm['on' + evType] = fn;//DOM 0
	}
}
function shield() {
	//return false;
}
document.oncontextmenu = shield;
/*document.addEventListener( "DOMContentLoaded", function () {
	document.body.onselectstart = shield;
	prettyPrint();
}, false );*/
addEvent(document,'DOMContentLoaded',function () {
	document.body.onselectstart = shield;
	prettyPrint();
}, false )
