# Android Architecture Components. Сохранение состояния Application
## Верстка ProfileActivity
Необходимо реализовать верстку экрана согласно макетам с использованием стилей (любым удобным для тебя способом (LinearLayout/RelativeLayout/ConstraintLayout)

```bash
Сверстай экран профиля пользователя, он должен содержать в себе следующие View:
Кнопка переключения в режим редактирования (ImageButton) @+id/btn_edit
Кнопка переключения режима Day/Night (ImageButton) @+id/btn_switch_theme
Аватар пользователя (ImageView/CircleImageView) @+id/iv_avatar
Псевдоним (TextView) @+id/tv_nick_name
Ранг (TextView) @+id/tv_rank
Рейтинг (TextView) @+id/tv_rating
Уважение (TextView) @+id/tv_respect
Имя (EditText) @+id/et_first_name
Фамилия (EditText) @+id/et_last_name
О себе (EditText) @+id/et_about
Обертка над "О себе" (TextInputLayout) @+id/wr_about
Репозиторий (EditText) @+id/et_repository
Обертка над "Репозиторий" (TextInputLayout) @+id/wr_repository
```

## Редактирование профиля
Реализуй бизнес логику режима редактирования профиля пользователя и сохранение измененных данных в SharedPreferences, режим редактирования должен сохраняться при перевороте экрана

```bash
Необходимо реализовать сохранение введенных данных пользователя (данные сохраняются при нажатии пользователем
кнопки сохранения данных (в EDIT_MODE @id/btn_edit)) с применением ViewModel и PreferencesRepository. Введенные
данные должны быть сохранены в SharedPreferences. Режим редактирования должен сохраняться при перевороте экрана
```

## Переключение режима Day/Night
Необходимо реализовать логику переключения между режимами Day/Night и сохранение активного режима в SharedPreferences
```bash
Реализуй переключение между режимами Day/Night при клике на кнопку @id/btn_switch_theme и установи дефолтное
значение режима из PreferencesRepository (сохраненное в SharedPreferences) в методе onCreate() класса App.
Атрибуты тем приложения colorAccentedSurface, сolorIcon, colorDivider
```
## *CircleImageView
Необходимо реализовать CustomView для скругления установленного Drawable
```bash
Реализуй CustomView с названием класса CircleImageView и кастомными xml атрибутами cv_borderColor (цвет границы
(format="color") по умолчанию white) и cv_borderWidth (ширина границы (format="dimension") по умолчанию 2dp).
CircleImageView должна превращать установленное изображение в круглое изображение с цветной рамкой, у
CircleImageView должны быть реализованы методы @Dimension getBorderWidth():Int, setBorderWidth(@Dimension dp:Int),
getBorderColor():Int, setBorderColor(hex:String), setBorderColor(@ColorRes colorId: Int). Используй
CircleImageView как ImageView для аватара пользователя (@id/iv_avatar)
```
## *SplashTheme
Необходимо реализовать тему, отображаемую при загрузке приложения до момента создания Activity
```bash
Реализуй SplashTheme в соответствии с макетами (@style/SplahTheme). Необходимо реализовать ее отображение при
запуске приложения до момента создания Activity. Как только Activity будет создана, необходимо установить AppTheme
```
## *firstName + lastName = nickName
Необходимо реализовать преобразование firstName и lastName пользователя в его nickName
```bash
Реализуй Profile.nickName как вычисляемое свойство из имени и фамилии пользователя, возвращающее значение
псевдонима пользователя в виде транслитерированной строки с заменой пробела на "_"
Пример:
Profile: firsName = "Женя", lastName = "Стереотипов"; Profile.nickName //Zhenya_Stereotipov
(Используй реализованный ранее метод Utils.transliteration)
```
## *Text Input Layout error
Необходимо реализовать вадидацию вводимых пользователем данных в поле @id/et_repository на соответствие url валидному github аккаунту
```bash
Реализуй валидацию вводимых пользователем данных в поле @id/et_repository на соответствие url валидному github
аккаунту, вводимое значение может быть пустой строкой или должно содержать домен github.com (https://, www,
https://www) и аккаунт пользователя (пути для исключения прикреплены в ресурсах урока). Если URL невалиден,
выводить сообщение "Невалидный адрес репозитория" в TextInputLayout (wr_repository.error(message)) и запрещать
сохранение невалидного значения в SharedPreferences (при попытке сохранить невалидное поле очищать et_repository
при нажатии @id/btn_edit)
Пример:
https://github.com/johnDoe //валиден
https://www.github.com/johnDoe //валиден
www.github.com/johnDoe //валиден
github.com/johnDoe //валиден
https://anyDomain.github.com/johnDoe //невалиден
https://github.com/ //невалиден
https://github.com //невалиден
https://github.com/johnDoe/tree //невалиден
https://github.com/johnDoe/tree/something //невалиден
https://github.com/enterprise //невалиден
https://github.com/pricing //невалиден
https://github.com/join //невалиден
```
## **Преобразование Инициалов в Drawable
Необходимо реализовать программное преобразование инициалов пользователя в Drawable с цветным фоном и буквами
```bash
Реализуй программное преобразование инициалов пользователя (если доступны - заполнено хотя бы одно поле) в
Drawable с фоном colorAccent (c учетом темы) и буквами инициалов (colorWhite) и установи полученное изображение
как изображение по умолчанию для профиля пользователя
```