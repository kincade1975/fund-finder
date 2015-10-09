angular.module('fundFinder')

	.service('User_CompanyService', function($http) {
		this.getCompany = function() {
			return $http.get('/api/v1/company');
		};
		this.setCompany = function(resource) {
			return $http.post('/api/v1/company', resource);
		};
		this.validateCompany = function(resource) {
			return $http.post('/api/v1/company/validate', resource);
		};
	});