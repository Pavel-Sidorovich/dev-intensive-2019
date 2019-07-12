# Компоненты платформы Android. Жизненный цикл Activity
## Сохранение состояния при пересоздании Activity
Необходимо реализовать сохранение состояния пользовательского ввода при пересоздании Activity
```bash
Реализуй сохранение введенного текста в поле EditText (et_message) при пересоздании Activity
```

## Bender.listenAnswer (positive case)
Необходимо реализовать метод listenAnswer класса Bender, принимающий в качестве аргумента ответ пользователя
и возвращающий Pair, содержащую следующий вопрос и цвет текущего статуса экземпляра класса Bender
```bash
Реализуй метод listenAnswer с сигнатурой listenAnswer(answer: String): Pair>.

Вопросы и ответы класса Bender, а также значения цветов статусов, прикреплены к ресурсам урока

Требования к методу:
При вводе верного ответа изменить текущий вопрос на следующий вопрос (question = question.nextQuestion()) 
и вернуть "Отлично - ты справился\n${question.question}" to status.color
Если вопросы закончились (Question.IDLE), вернуть "Отлично - ты справился\nНа этом все, вопросов больше нет"
Необходимо сохранять состояние экземпляра класса Bender при пересоздании Activity 
(достаточно сохранить Status, Question)

Пример:
//Как меня зовут?
benderObj.listenAnswer("Bender") //Отлично - ты справился\nНазови мою профессию?

//Мой серийный номер?
benderObj.listenAnswer("2716057") //Отлично - ты справился\nНа этом все, вопросов больше нет

//Как меня зовут?
benderObj.listenAnswer("Bender") //Отлично - ты справился\nНазови мою профессию?
//onPause() -> onStop() -> onDestroy() -> onCreate()
//Назови мою профессию?
```

## Bender.listenAnswer (negative case)
Необходимо реализовать метод listenAnswer класса Bender, принимающий в качестве аргумента ответ пользователя
и возвращающий Pair, содержащую текст ошибки и цвет следующего статуса экземпляра класса Bender
```bash
Реализуй метод listenAnswer со следующей сигнатурой listenAnswer(answer: String): Pair.

Вопросы и верные ответы, а также значения цветов статусов, прикреплены к ресурсам урока

Требования к методу:
При вводе неверного ответа изменить текущий статус на следующий статус (status = status.nextStatus()),
вернуть "Это неправильный ответ\n${question.question}" to status.color и изменить цвет ImageView (iv_bender)
на цвет status.color (метод setColorFilter(color,"MULTIPLY"))
При вводе неверного ответа более 3 раз сбросить состояние сущности Bender на значение по умолчанию 
(status = Status.NORMAL, question = Question.NAME) и вернуть "Это неправильный ответ. Давай все по
новой\n${question.question}" to status.color и изменить цвет ImageView (iv_bender) на цвет status.color
Необходимо сохранять состояние экземпляра класса Bender при пересоздании Activity (достаточно сохранить
Status, Question)

Пример:
//Как меня зовут? #NORMAL(Triple(255, 255, 255))
benderObj.listenAnswer("Fry") //Это неправильный ответ\nКак меня зовут? #WARNING(Triple(255, 120, 0))

//Мой серийный номер? #CRITICAL(Triple(255, 0, 0))
benderObj.listenAnswer("0000000") //Это неправильный ответ. Давай все по новой\nКак меня зовут? 
#NORMAL(Triple(255, 255, 255))

//Как меня зовут? #WARNING(Triple(255, 120, 0))
benderObj.listenAnswer("Fry") //Это неправильный ответ\nКак меня зовут? #CRITICAL(Triple(255, 0, 0))
//onPause() -> onStop() -> onDestroy() -> onCreate()
//Как меня зовут? #CRITICAL(Triple(255, 0, 0))
```
## *Activity.hideKeyboard
Необходимо реализовать extension для скрытия Software Keyboard
```bash
Реализуй extension Activity.hideKeyboard(), скрывающую экранную клавиатуру
```
## *actionDone
Необходимо реализовать кнопку DONE в Software Keyboard, при нажатии на которую будет происходить 
отправка сообщения в экземпляр класса Bender и скрытие клавиатуры
```bash
Реализуй кнопку DONE в Software Keyboard (imeOptions="actionDone"), при нажатии на которую будет
происходить отправка сообщения в экземпляр класса Bender и скрытие клавиатуры. Для этого реализуй
OnEditorActionListener для EditText (et_message)
```
## *Проверка правильности формата ответов
Необходимо реализовать проверку вводимых пользователем ответов на соответствие условиям валидации
для каждого типа вопроса
```bash
Реализуй проверку вводимых пользователем ответов на соответствие условиям валидации для каждого
типа вопроса (валидация НЕ влияет на Status)
Question.NAME -> "Имя должно начинаться с заглавной буквы"
Question.PROFESSION -> "Профессия должна начинаться со строчной буквы"
Question.MATERIAL -> "Материал не должен содержать цифр"
Question.BDAY -> "Год моего рождения должен содержать только цифры"
Question.SERIAL -> "Серийный номер содержит только цифры, и их 7"
Question.IDLE -> //игнорировать валидацию

Пример:
//Как меня зовут? #NORMAL(Triple(255, 255, 255))
benderObj.listenAnswer("bender") //Имя должно начинаться с заглавной буквы\nКак меня зовут? 
#NORMAL(Triple(255, 255, 255))

//Отлично - ты справился\nНа этом все, вопросов больше нет #NORMAL(Triple(255, 255, 255))
benderObj.listenAnswer("any text") //На этом все, вопросов больше нет 
#NORMAL(Triple(255, 255, 255))
```
## **Activity.isKeyboardOpen Activity.isKeyboardClosed
Необходимо реализовать extension для проверки, открыта или нет Software Keyboard
```bash
Реализуй extension для проверки, открыта или нет Software Keyboard с применением метода 
rootView.getWindowVisibleDisplayFrame(Rect())
```
