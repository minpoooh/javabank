$(function() {
    $("header .setting_btn").on('click', function(e){
	    e.stopPropagation();
	    $("header .setting_box, .dimm").toggle();
	});
	$(document).on('click', function(e){
	    if(!$(e.target).closest('.setting_box, .setBtn').length){
	        $(".setting_box, .dimm").hide();
	    }
	});
	$("header .setting_box li:last-child").on('click', function() {
	    $(".setting_box, .dimm").hide();
	});

	// popup foreach 통합
    $(".popup_btn").each(function() {
        $(this).on('click', function(e) {
            e.preventDefault();
            let popupId = $(this).data('popup');
        
            $('#' + popupId).addClass('s_active');
            $('.dimm').addClass('s_active');
        });

		$(".close_btn").on('click', function() {
			let popupId = $(this).data('popup');
			console.log(popupId)

			$('#' + popupId).removeClass('s_active');
            $('.dimm').removeClass('s_active');
			$("input").val("");
		});
    });
});
