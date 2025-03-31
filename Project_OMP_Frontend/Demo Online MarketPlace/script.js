$(document).ready(function(){

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8090/OMP/viewAllProducts',
        success: function(products){
            let productHtml = '';
            $.each(products, function(index, product){
                productHtml += `<div class="product" data-id="${product.productid}">
                                    <div class="productimage">
                                        <img src="http://localhost:8090${product.imageUrl}" alt="${product.name}">
                                    </div>
                                    <div class="productdescription">
                                        <p> ${product.name}  </p>
                                    </div>
                                </div>`
            });
            $("#product-list").html(productHtml);
        },
        error: function(){
            alert("Failed to load products");
        }
    });

     $(document).on("click", ".product", function(){
         let productId = $(this).data("id");
         window.location.href=`productdetails.html?id=${productId}`
     });
});


$(document).ready(function(){
    if($("#product-details").length){
        function getQueryParam(name){
            const urlParams=new URLSearchParams(window.location.search);
            return urlParams.get(name);
        }

        const productId=getQueryParam("id");

        if(productId){
            $.ajax({
                type: 'GET',
                //dataType: 'json',
                url: `http://localhost:8090/OMP/viewProductDetails/${productId}`,
                success: function(product){
                    let productHtml = `<div class="product-image-container">
    <div>
        <h3>${product.name}</h3>
    </div>
        <img src="http://localhost:8090/OMP/product/image/${product.productid}" alt="${product.name}" class="product-image">
    <div>
        <p class="product-meta"  id="product-meta">
        ${product.name} <br> 200 Subscribers <br> 4 Stars 
        </p>
    </div>
</div>

<div class="product-desc-container">
    <ul class="product-description">
        <li><i>${product.description}</i></li>
    </ul>

        <div class="button-container">
            <button class="subscribe-btn">Subscribe</button>
            <button class="review-btn"> Add a Review</button>
        </div>
</div>

<div class="product-review-container">
    <h3>Users Reviews</h3>
    <div class="review-card">
        <p><strong>Saranya – 5 Stars</strong></p>
        <p><em>This product is so tasty and worth to have it.</em></p>
    </div>
    <button class="more-reviews-btn">Click For More</button>
</div>`
                    $("#product-details").html(productHtml);
                },
                error: function(){
                    $("#product-details").html("<p> Product Not Found </p>");
                }
            });
        } else {
            $("#product-details").html("<p>Invalid Product ID</p>");
        }
    }
});