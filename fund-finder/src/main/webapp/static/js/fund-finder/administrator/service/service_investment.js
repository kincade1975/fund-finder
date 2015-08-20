angular.module('fundFinder')

	.service('Administrator_InvestmentService', function($http) {
		this.getPage = function(resource) {
			return $http.post('/api/v1/investment/page', resource);
		};
		this.getInvestments = function() {
			return $http.get('/api/v1/investment');
		};
		this.getInvestment = function(id) {
			return $http.get('/api/v1/investment/' + id);
		};
		this.saveInvestment = function(resource) {
			return $http.post('/api/v1/investment', resource);
		};
		this.deleteInvestment = function(id) {
			return $http.delete('/api/v1/investment/' + id);
		};
	});