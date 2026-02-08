function updateOrderTotal() {
	let total = 0;

	document.querySelectorAll('.total-input').forEach(input => {
		total += parseFloat(input.value) || 0;
	});

	document.getElementById('order-total').textContent =
		'$' + total.toFixed(2);
}

document.addEventListener('click', function (e) {

	if (!e.target.classList.contains('btn-minus') &&
		!e.target.classList.contains('btn-plus')) {
		return;
	}

	const row = e.target.closest('tr');
	if (!row) return;

	const qtyInput = row.querySelector('.qty-input');
	const price = parseFloat(row.dataset.price);

	let quantity = parseInt(qtyInput.value) || 0;

	if (e.target.classList.contains('btn-minus') && quantity > 0) {
		quantity--;
	}

	if (e.target.classList.contains('btn-plus')) {
		quantity++;
	}

	qtyInput.value = quantity;

	const rowTotal = price * quantity;

	row.querySelector('.row-total').textContent =
		'$' + rowTotal.toFixed(2);

	row.querySelector('.total-input').value =
		rowTotal.toFixed(2);

	updateOrderTotal();
});

window.addEventListener('load', updateOrderTotal);
