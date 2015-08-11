angular.module('fundFinder')

// ==============================================================================================================
// 	OVERVIEW
// ==============================================================================================================
.controller('User_ArticleOverviewCtrl', function($rootScope, $scope, $state, $sce, User_ArticleService) {
	
	User_ArticleService.getArticles().
		success(function(data, status, headers, config) {
			$scope.articles = data;
		}).
		error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	$scope.toTrusted = function(html) {
	    return $sce.trustAsHtml(html);
	}
	
	$scope.showArticle = function(entity) {
		$state.go('user.article_show', { 'id' : entity.id });
	};
	
})

.controller('User_ArticleShowCtrl', function($rootScope, $scope, $state, $stateParams, $sce, User_ArticleService) {

	$scope.back = function() {
		if ($rootScope.previousState) {
			$state.go($rootScope.previousState, $rootScope.previousStateParams);
		} else {
			window.history.back();
		}
	};
	
	$scope.toTrusted = function(html) {
	    return $sce.trustAsHtml(html);
	}
	
	User_ArticleService.getArticle($stateParams.id)
		.success(function(data, status) {
			$scope.article = data;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja članka');
			}
		});
	
});
