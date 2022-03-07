$(document).ready(function(){
    $('#addToCart').on('click', function (event){
     addToCart();
    })
})


function addToCart(){
    quantity = $('#quantity' + productId).val();
    url = contextPath +'cart/add/' + productId + '/' + quantity;
    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr){
            xhr.setRequestHeader(csrfHeader, csrfValue);
        }
    }).done(function(response){
        showModalDialog("Giỏ Hàng", response);
    }).fail(function(data){
       showModalDialog("Lỗi","Có lỗi khi thêm giỏ hàng." )
    });

}