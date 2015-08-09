angular.module('fundFinder')

	.service('Administrator_TotalService', function($rootScope, $state, $http) {
		this.updateTotal = function() {
			$http.get('/api/v1/total')
				.success(function(data, status, headers, config) {
					$rootScope.totalUsers = data.totalUsers;
					$rootScope.totalTenders = data.totalTenders;
					$rootScope.totalInvestments = data.totalInvestments;
					$rootScope.totalArticles = data.totalArticles;
				})
				.error(function(data, status, headers, config) {
					if (status == 403) {
						$state.go('login');
					} else {
						toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
					}
				});
		};
	});