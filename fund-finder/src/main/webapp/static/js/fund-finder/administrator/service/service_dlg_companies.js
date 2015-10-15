angular.module('fundFinder')

	.service('Administrator_DlgCompaniesService', function($http) {
		this.getCompanies = function(type, label) {
			return $http.get('/api/v1/dashboard/companies?type=' + type + "&label=" + label);
		};
	});