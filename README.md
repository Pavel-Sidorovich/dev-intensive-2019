# Kotlin на практике. Первое знакомство
## DTO User, Factory 
Необходимо создать data класс User и реализовать Factory для создания экземпляров класса 

```bash
  Создай data class User содержащий сделующие свойства: 
val id : String, 
var firstName : String?,
var lastName : String?, 
var avatar : String?,
var rating : Int = 0, 
var respect : Int = 0, 
var lastVisit : Date? = Date(),
var isOnline : Boolean = false

  Реализуй паттерн Factory с методом makeUser(fullName) принимающий в качесте аргумента полное имя пользователя и
возвращающий экземпляр класса User
```
## Base Message, AbstractFactory 
Необходимо создать абстрактный класс BaseMessage и два его наследника TextMessage и ImageMessage. Реализовать 
AbstractFactory для создания экземпляров классов наследников 
```bash
  Необходимо создать абстрактный класс BaseMessage содержащий сделующие свойства: 
val id: String,
val from: User?,
val chat: Chat,
val isIncoming: Boolean = false,
val date: Date = Date()
и абстрактный метод formatMessage() - возвращает строку содержащюю информацию о id сообщения, имени 
получателя/отправителя, виде сообщения ("получил/отправил") и типе сообщения ("сообщение"/"изображение")
  Реализуй паттерн AbstractFactory с методом makeMessage(from, chat, date, type, payload, isIncoming = false)
принимающий в качесте аргументов пользователя создавшего сообщение, чат к которому относится сообщение, дата
сообщения и его тип ("text/image"), полезную нагрузку
Пример:
BaseMessage.makeMessage(user, chat, date, "any text message", "text") //Василий отправил сообщение "any text
message" только что
BaseMessage.makeMessage(user, chat, date, "https://anyurl.com", "image",true) //Василий получил изображение 
"https://anyurl.com" 2 часа назад
```
## parseFullName 
Необходимо реализовать утилитный метод parseFullName(fullName) принимающий в качестве аргумента полное имя пользователя и возвращающий пару значений "firstName lastName" 
```bash
  Реализуй метод Utils.parseFullName(fullName) принимающий в качестве аргумента полное имя пользователя 
(null, пустую строку) и возвращающий пару значений Pair(firstName, lastName) при невозможности распарсить полное
имя или его часть вернуть null null/"firstName" null 
Пример:
Utils.parseFullName(null) //null null
Utils.parseFullName("") //null null
Utils.parseFullName(" ") //null null
Utils.parseFullName("John") //John null
```
## Date.format 
Необходимо реализовать extension для форматирования вывода даты экземпляра класса Date по заданному паттерну 
```bash
  Реализуй extension Date.format(pattern) возвращающий отформатированную дату по паттерну передаваемому в качестве
аргумента (значение по умолчанию "HH:mm:ss dd.MM.yy" локаль "ru")
Пример:
Date().format() //14:00:00 27.06.19
Date().format("HH:mm") //14:00
```
## Date.add 
Необходимо реализовать extension для изменения значения экземпляра Data (добавление/вычитание) на указанную временную единицу 
```bash
  Реализуй extension Date.add(value, TimeUnits) добавляющий или вычитающий значение переданное первым 
аргументом в единицах измерения второго аргумента (enum TimeUnits [SECOND, MINUTE, HOUR, DAY]) и возвращающий
модифицированный экземпляр Date 
Пример:
Date().add(2, TimeUnits.SECOND) //Thu Jun 27 14:00:02 GST 2019
Date().add(-4, TimeUnits.DAY) //Thu Jun 23 14:00:00 GST 2019
```
## *toInitials 
Необходимо реализовать утилитный метод toInitials(firstName lastName) принимающий в качестве аргументов имя и фамилию пользователя и возвращающий его инициалы 
```bash
  Реализуй метод Utils.toInitials(firstName lastName) принимающий в качестве аргументов имя и фамилию
пользователя (null, пустую строку) и возвращающий строку с первыми буквами имени и фамилии в верхнем
регистре (если один из аргументов null то вернуть один инициал, если оба аргумента null вернуть null)
Пример:
Utils.toInitials("john" ,"doe") //JD
Utils.toInitials("John", null) //J
Utils.toInitials(null, null) //null
Utils.toInitials(" ", "") //null
```
## *transliteration 
Необходимо реализовать утилитный метод transliteration(payload divider) принимающий в качестве аргумента строку и возвращающий преобразованную строку из латинских символов 
```bash
Реализуй метод Utils.transliteration(payload divider) принимающий в качестве аргумента строку (divider 
по умолчанию " ") и возвращающий преобразованную строку из латинских символов, словарь символов соответствия
алфовитов доступен в ресурсах к заданию
Пример:
Utils.transliteration("Женя Стереотипов") //Zhenya Stereotipov
Utils.transliteration("Amazing Петр","_") //Amazing_Petr
```
## *Date.humanizeDiff 
Необходимо реализовать extension для форматирования вывода разницы между текущим экземпляром Date и текущим моментом времени
(или указанным в качестве аргумента) в человекообразном формате 
```bash
  Реализуй extension Date.humanizeDiff(date) (значение по умолчанию текущий момент времени) для
форматирования вывода разницы между датами в человекообразном формате, с учетом склонения 
числительных.
Пример:
Date().add(-2, TimeUnits.HOUR).humanizeDiff() //2 часа назад
Date().add(-5, TimeUnits.DAY).humanizeDiff() //5 дней назад
Date().add(2, TimeUnits.MINUTE).humanizeDiff() //через 2 минуты
Date().add(7, TimeUnits.DAY).humanizeDiff() //через 7 дней
Date().add(-400, TimeUnits.DAY).humanizeDiff() //более года назад
Date().add(400, TimeUnits.DAY).humanizeDiff() //более чем через год
```
## **Паттерн Builder 
Необходимо реализовать Builder для класса User 
```bash
  Реализуй паттерн Builder для класса User. 
User.Builder().id(s)
.firstName(s)
.lastName(s)
.avatar(s)
.rating(n)
.respect(n)
.lastVisit(d)
.isOnline(b)
.build() должен вернуть объект User
```
## **plural 
Необходимо реализовать метод plural для enum TimeUnits 
```bush
  Реализуй метод plural для всех перечислений TimeUnits вида TimeUnits.SECOND.plural(value:Int) возвращающую
значение с правильно склоненной единицой измерения
Пример:
TimeUnits.SECOND.plural(1) //1 секунду
TimeUnits.MINUTE.plural(4) //4 минуты
TimeUnits.HOUR.plural(19) //19 часов
TimeUnits.DAY.plural(222) //222 дня
```
## *String.truncate 
Необходимо реализовать метод truncate усекающий исходную строку до указанного числа символов и добавляющий заполнитель "..." в конец строки 
```bush
  Реализуй extension усекающий исходную строку до указанного числа символов (по умолчанию 16) и возвращающий
усеченную строку с заполнителем "..." если последний символ усеченной строки является пробелом - удалить его
и добавить заполнитель
Пример:
"Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate() //Bender Bending Ro...
"Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate(14) //Bender Bending...
```
## *String.stripHtml 
Необходимо реализовать метод stripHtml для очистки строки от лишних пробелов, html тегов, escape последовательностей 
```bush
  Реализуй extension позволяющий очистить строку от html тегов и html escape последовательностей ("& < > ' ""),
а так же удалить пустые символы (пробелы) между словами если их больше 1. Необходимо вернуть модифицированную
строку
Пример:
"<p class="title">Образовательное IT-сообщество Skill Branch</p>".stripHtml() 
//Образовательное IT-сообщество Skill Branch
"<p>Образовательное       IT-сообщество Skill Branch</p>".stripHtml() 
//Образовательное IT-сообщество Skill Branch
```
