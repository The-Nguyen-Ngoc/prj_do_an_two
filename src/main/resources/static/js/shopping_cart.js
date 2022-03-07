$(document).ready(function () {
    $('.linkMinus').on('click', function (e) {
        e.preventDefault();
        decreaseQuantity($(this));
    });

    $('.linkPlus').on('click', function (e) {
        e.preventDefault();
        increaseQuantity($(this));
    });

    $('.remove_item').on('click', function (e) {
        e.preventDefault();
        removeProduct($(this));
    });
});

function decreaseQuantity(link) {
    productId = link.attr('pid');
    quantityInput = $("#quantity" + productId);
    newQuantity = parseInt(quantityInput.val()) - 1;

    if (newQuantity > 0) {
        quantityInput.val(newQuantity);
        updateQuantity();
    } else {
        showModalDialog("Thông Báo", "Số lượng sản phẩm không được nhỏ hơn 1");
    }
}

function increaseQuantity(link) {
    productId = link.attr('pid');
    quantityInput = $("#quantity" + productId);
    newQuantity = parseInt(quantityInput.val()) + 1;

    if (newQuantity < 6) {
        quantityInput.val(newQuantity);
        updateQuantity();

    } else {
        showModalDialog("Thông Báo", "Số lượng sản phẩm không được lớn hơn 5");
    }
}

function updateQuantity() {
    quantity = $('#quantity' + productId).val();
    url = contextPath + 'cart/update/' + productId + '/' + quantity;
    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfValue);
        }
    }).done(function (response) {
        updateSubtotal(response, productId)
        updateTotal();
    }).fail(function (data) {
        showModalDialog("Lỗi", "Có lỗi khi cập nhật giỏ hàng.")
    });

}

function updateSubtotal(updatedSubtotal, productId) {
    // formattedSubtotal = $.number(updatedSubtotal, 2);
    $('#subtotal' + productId).text(updatedSubtotal);
}

function showEmptyShoppingCart() {
    $('#sectionTotal').hide();
    $('#sectionEmptyCartMessage').removeClass('d-none');
}

function updateTotal() {
    total = 0.0;
    productCount = 0;
    $('.subtotal').each(function (index, element) {
        productCount++;
        total += parseFloat(element.innerHTML);

    });

    if (productCount < 1) {
        showEmptyShoppingCart();
    }

    formattedTotal = $.number(total, 2);
    $('#total').text(formattedTotal);

}

function removeProduct(link) {

    url = link.attr('href');
    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfValue);
        }
    }).done(function (response) {
        showModalDialog("Thông báo", response);
        rowNumber = link.attr('rowNumber');
        removeProductHTML(rowNumber);
        updateTotal();
        updateCountNumbers();
    }).fail(function (data) {
        showModalDialog("Lỗi", "Có lỗi khi cập nhật giỏ hàng.")
    });
}

function removeProductHTML(rowNumber) {
    $('#row' + rowNumber).remove();
}

function updateCountNumbers() {
    $(".divCount").each(function (index, element) {
        element.innerHTML = "" + (index + 1);
    });
}