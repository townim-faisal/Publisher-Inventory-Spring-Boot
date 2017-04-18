$(document).ready(function() {
    $("#dialog").dialog({
        autoOpen: false,
        modal: true
    });
});

$(".confirm-delete").click(function(e) {
    e.preventDefault();

    $('.serial-url').html('Serial no : <strong>'+$(this).attr('serial')+'</strong>');
    $('.name-url').html('<strong>'+$(this).attr('name')+'</strong>');

    var targetUrl = $(this).attr("href");

    $("#dialog").dialog({
        buttons : {
            "Confirm" : function() {
                window.location.href = targetUrl;
            },
            "Cancel" : function() {
                $(this).dialog("close");
            }
        }
    });

    $("#dialog").dialog("open");
});
