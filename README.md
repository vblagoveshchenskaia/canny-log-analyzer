# Canny log analyzer

[englsh - click here](#eng)

## Rus

Canny log analyzer - это приложение для анализа и сравнения логов CAN шины, сохраненных с помощью Canny Can (Lin)
Monitor.

Для каждого лога приложение находит отдельные биты, которые не менялись на его протяжении, а при сравнении логов -
выбирает из них те, которые в разных логах имеют разные значения и генерирует отчет. Таким образом, если есть два, или
более логов, снятых в разных состояниях системы, можно найти биты, передающие это состояние.

### Пример отчета

```
0.txt
           0x0f0	10000000	____0001	00000000	00000000	___1____	11100100	00000000	_0_010__
           0x0f5	____0___	____0101	01010101	01010101	01010000	00000000	00000000	00000000
1.txt
           0x0f0	10000000	____0010	00000000	00000000	___1____	11100100	00000000	_0_0_011
2.txt
           0x0f0	10000000	____1011	00000000	00000000	___0____	11100100	00000000	_0_01011
           0x0f5	________	____0101	01010101	01010101	01010000	00000_00	00000000	00000000

Comparing 1.txt
           0x0f0	10000000	____0010	00000000	00000000	___1____	11100100	00000000	_0_0_011
           0.txt	________	______01	________	________	________	________	________	________
           2.txt	________	____1__1	________	________	___0____	________	________	________

           0x0f5	not present
           0.txt	____0___	____0101	01010101	01010101	01010000	00000000	00000000	00000000
           2.txt	________	____0101	01010101	01010101	01010000	00000_00	00000000	00000000
```

В таблицах в первом столбце указан идентификатор кадра, или, имя лога, в остальных - содержимое кадра.

В данном примере мы сравниваем файл 1.txt с 0.txt и 2.txt и видим, например, что:

- 8-й бит 2 байта кадра с идентификатором 0x0f0 всегда равен 0 в 1.txt и всегда равен 1 в 0.txt и 2.txt
- кадр с идентификатором 0x0f5 отсутствует в 1.txt

### Использование

Сначала нужно снять и сохранить логи с помощю Canny Can (Lin) Monitor так, чтобы интересующее состояние (режим,
положение переключателя, ...) было одинкаковым на протяжении каждого отдельного лога, но разным в разных логах.

1. Скачайте последнюю версию приложения - файл canny-log-analyzer-{latest}.zip из раздела Releases ->
   {latest} (https://github.com/vblagoveshchenskaia/canny-log-analyzer/releases/latest) -> Assets и разархивируйте в
   любое удобное место;
2. Запустите exe-файл. Для работы приложения требуется Java Runtime Environment минимум 17 версии, если она не
   установлена, вы будете перенаправлены на страницу загрузки;
3. Нажмите клавишу Analyze и выберите файл, или файлы для сравнения;
4. Если было выбрано более одного файла - выберите из списка тот, который хотите сравнить с остальными;
5. Отчет отобразится на экране. Его можно сохранить с помощью клавишы Save.

## Eng

Canny log analyzer is an application for analysis and comparison of CAN-bus logs, retrieved and saved with Canny Can (
Lin) Monitor.

For each log the application finds individual bits, which remained unchanged during the log recording, and while
comparing multiple logs - selects from them the ones, which have distinct values in different logs and generates a
report. So, if you have one or more logs, recorded in different system states, you can find the bits, which represent
this state.

### Report example

[report example](#пример-отчета)
In the tables, the frame id or log name is in the first column, and the frame content - in the others.

In this example we have compared file 1.txt against 0.txt and 2.txt, and we can see, that:

- 8th bit of 2nd byte of frame with id 0x0f0 is always 0 in 1.txt and 1 in both 0.txt and 2.txt
- frame with id 0x0f5 is not present in 1.txt

### Usage

1. Download the last version - canny-log-analyzer-{latest}.zip from Releases ->
   {latest} (https://github.com/vblagoveshchenskaia/canny-log-analyzer/releases/latest) -> Assets and unpack to any
   convenient location;
2. Run the exe file. In order to work, this application needs Java Runtime Environment 17 or later, if it is not
   installed, you will be redirected to a download page;
3. Press Analyze and select file or files for comparison;
4. If you have selected more, then one file - choose the one to compare with others
5. The report will be dispayed. You can save it by pressing the Save button.

### Build from source

Having JDK 17+ and Maven 3.9+, just run `mvn package`
