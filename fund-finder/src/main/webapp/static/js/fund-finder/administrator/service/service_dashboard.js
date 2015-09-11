angular.module('fundFinder')

	.service('Administrator_DashboardService', function($http) {
		this.getLatestUsers = function() {
			return $http.get('/api/v1//dashboard/users');
		};
		this.getLatestTenders = function() {
			return $http.get('/api/v1//dashboard/tenders');
		};
	});