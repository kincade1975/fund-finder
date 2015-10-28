angular.module('fundFinder')

	.service('User_ImpressionService', function($http) {
		this.setImpression = function(resources) {
			return $http.post('/api/v1/impression', resources);
		};
	});