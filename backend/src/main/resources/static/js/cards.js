const myForm = document.getElementById('myForm');
const submitButton = document.getElementById('submitButton');
const cardId = '@{/cards/{id}(id=${card.id})}'/* Ваш код для получения значения card.id */;

submitButton.addEventListener('click', (event) => {
    event.preventDefault(); // Предотвращаем отправку формы (для примера)

    const formAction = `/cards/${cardId}`; // Здесь формируем нужный URL в соответствии с вашими требованиями
    myForm.action = formAction;

    myForm.submit(); // Отправляем форму
});