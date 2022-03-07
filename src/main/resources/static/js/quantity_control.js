$(document).ready(function() {
    $('.linkMinus').on('click', function(e) {
        e.preventDefault();
        decreaseQuantity($(this));
    });

    $('.linkPlus').on('click', function(e) {
        e.preventDefault();
        increaseQuantity($(this));
    });
});

function decreaseQuantity(link){
    productId =link.attr('pid');
    quantityInput = $("#quantity"+  productId);
    newQuantity = parseInt(quantityInput.val()) - 1;

    if(newQuantity >0){
        quantityInput.val(newQuantity);
    }else {
        showModalDialog("Thông Báo", "Số lượng sản phẩm không được nhỏ hơn 1");
    }
}

function increaseQuantity(link){
    productId = link.attr('pid');
    quantityInput = $("#quantity"+  productId);
    newQuantity = parseInt(quantityInput.val()) + 1;

    if(newQuantity <6){
        quantityInput.val(newQuantity);
    }else {
        showModalDialog("Thông Báo", "Số lượng sản phẩm không được lớn hơn 5");
    }
}