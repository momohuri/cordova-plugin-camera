var CustomCamera = {
	getPicture: function(success, failure){
		cordova.exec(success, failure, "CustomCamera", "takePicture", []);
	}
};
module.exports = CustomCamera;