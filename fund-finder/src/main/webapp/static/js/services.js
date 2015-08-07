angular.module('fundFinder')

	//==============================================================================================================
	// 	Total Service
	// ==============================================================================================================
	.service('TotalService', function($rootScope, $state, $http) {
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
	})

	// ==============================================================================================================
	// 	User Service
	// ==============================================================================================================
	.service('UserService', function($http) {
		this.getPage = function(uiGridResource) {
			return $http.post('/api/v1/user/page', uiGridResource);
		};
		this.deleteUser = function(id) {
			return $http.delete('/api/v1/user/' + id);
		};
	})
	
	// ==============================================================================================================
	// 	Investment Service
	// ==============================================================================================================
	.service('InvestmentService', function($http) {
		this.getPage = function(resource) {
			return $http.post('/api/v1/investment/page', resource);
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
	})
	
	// ==============================================================================================================
	// 	Article Service
	// ==============================================================================================================
	.service('ArticleService', function($http) {
		this.getPage = function(resource) {
			return $http.post('/api/v1/article/page', resource);
		};
		this.getArticle = function(id) {
			return $http.get('/api/v1/article/' + id);
		};
		this.saveArticle = function(resource) {
			return $http.post('/api/v1/article', resource);
		};
		this.deleteArticle = function(id) {
			return $http.delete('/api/v1/article/' + id);
		};
	});