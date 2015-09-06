angular.module('fundFinder')

	.service('User_OpportunityService', function($http) {
		this.findTenders = function() {
			return $http.get('/api/v1/opportunity');
		};
	});