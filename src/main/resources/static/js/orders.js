function confirmDeliver() {
	return confirm(
		'¿Estás seguro de que deseas marcar esta orden como ENTREGADA?\n\n' +
		'Esta acción no se puede deshacer.'
	);
}

function confirmCancel() {
	return confirm(
		'¿Estás seguro de que deseas marcar esta orden como CANCELADA?\n\n' +
		'Esta acción no se puede deshacer.'
	);
}