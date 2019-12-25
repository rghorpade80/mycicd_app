searchApp.service('graphSevice', ['$http', function($http){
	return{
		getGraphData : function(url,meaResultId){
			return $http({
				method : 'GET',
				url : url,
				params: {
					meaId : meaResultId 
				}
			}).then(function successCallback(response) {
				$.LoadingOverlay("hide", spinner);
				return response.data;
			},
			function errorCallback(response) {
				$.LoadingOverlay("hide", spinner);
				return response; 
			})
		}
	}
}]);