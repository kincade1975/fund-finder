angular.module('fundFinder')

	.service('User_InvestmentService', function($http) {
		this.getInvestments = function() {
			return $http.get('/api/v1/investment/user');
		};
		this.setInvestments = function(resources) {
			return $http.post('/api/v1/investment/user', resources);
		};
	});