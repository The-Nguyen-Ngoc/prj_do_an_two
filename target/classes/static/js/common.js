
// logout navbar
$(document).ready(()=>{
    $("#logoutLink").on('click',(e)=>{
        e.preventDefault();
        $('#logoutForm').submit();

    })
})