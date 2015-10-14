angular.module('fundFinder')

.service('Administrator_StatisticsService', function($http) {
	
	this.getCompaniesBySector = function() {
		return $http.get('/api/v1/statistics/companiesBySector');
	};
	
	this.getCompaniesByLocation = function() {
		return $http.get('/api/v1/statistics/companiesByLocation');
	};
	
	this.getInvestments = function() {
		return $http.get('/api/v1/statistics/investments');
	};
	
	this.getRevenues = function() {
		return $http.get('/api/v1/statistics/revenues');
	};
	
});