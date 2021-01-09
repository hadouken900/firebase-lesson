# Firebase Firestore



Firebase Firestore - это база данных, которая изменяется в реальном времени и хранит данные в формате JSON.
Любые изменения тут же синхронизируются между всеми клиентами, или девайсами, которые используют одну и ту же базу данных.
Другими словами, обновление в Firebase происходят мгновенно.

Полезные ссылки:
* [Google Firebase]
* [Firestore Documentation]

# Теория

Для того чтобы начать работать с Firestore нужно получить экземпляр класса FirebaseFirestore:
```java
FirebaseFirestore db = FirebaseFirestore.getInstance();
```
Объект db представляет из себя входную точку через которую нужно проводить все операции с базой.

>В Firestore для хранения данных используются коллекции и документы. Документ — это запись, которая содержит какие-либо поля. Документы объединяются в коллекции. 
Также документ может содержать вложенные коллекции, но на андроиде это не поддерживается.
Если проводить аналогию с SQL-базой, то коллекция — это таблица, а документ — это запись в этой таблице. 
Одна коллекция может содержать документы с разным набором полей.

- Добавление данных:
```java
//db может сохранить данные лежащие в Map
Map<String,Object> data = new HashMap<>();
String s = "some string";
data.put("text",s);

//Сохранить данные можно так
db.collection("Название коллекции").document("Название документа").set(data);
```
![a](https://sun9-35.userapi.com/impf/0u4jb-FkQxp8L-7BQxS_5JxLeMTTfIq4cSqDGw/xTZ7ca0Sve8.jpg?size=1600x399&quality=96&proxy=1&sign=2419aad2fe9c512434c95cd784e606e3&type=album)

```java
//Или так. В этом случае название документа будет автоматически сгенерировано Firestore
db.collection("Название коллекции").add(data);
```
![b](https://sun9-4.userapi.com/impf/zeFEYBEYSFmvzS1vAAlG8ouf_0Z1J_usa1_hGg/jZFipkYmZzQ.jpg?size=1684x313&quality=96&proxy=1&sign=1605599305ab1ea8b040871575a41ae5&type=album)

- Получение данных :
```java

//для получения всех документов в коллекции
//так как при исполнении операции может произойти ошибка, нужно добавить специальный слушатель, в котором, в зависимости от результата, мы будем выполнять разные действия
db.collection("Название коллекции")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            
                //если успешно, то выводим в лог содержимое каждого документа
                //если нет, то сообщаем об ошибке
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {                    
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
        
//соответственно для получения одного документа 
db.collection("Название коллекции").document("название документа").get().addOnCompleteListener(...)
```

- Удаление:
```java
//тут всё достаточно просто
db.collection("Название коллекции").document("название документа").delete();
```


- Получение информации от бд в реальном времени
Наше приложение может "подписаться" на изменения какой-то коллекции в базе. Для этого нужно переопределить метод OnStart()
```java

@Override
    protected void onStart() {
        super.onStart();
        
        
        db.collection("Название коллекции").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                
                //измененный список всех документов в коллекции
                List<DocumentSnapshot> documents = value.getDocuments();
               
                //пройтись по 
                for (DocumentSnapshot doc : documents) {
                    //получаем данные из документа 
                    String str = doc.getString("text");
                    
                    //далее выполняем все нужные операции над данными
                    //...
                }
                
            }
        });
    }
```






# Подготовка:
- войти в гугл аккаунт на firebase.google.com
- перейти в консоль 
- создать проект
- подключить приложение к проекту
- создать firebase firestore в тестовом режиме
- скопировать файлы из этого репозитория в ваш проект
- поменять строчку package в каждом java файле

# Задания:
- 1 - добавление сообщения в firestore
- 2 - добавление SnapshotListener для автоматического обновления списка сообщений
- 3 - удаление сообщения из firestore
- 4 - отсортировать сообщения на экране по дате
- 5 - (дополнительно) изменить макеты

  [Google Firebase]: <https://firebase.google.com>
  [Firestore Documentation]: <https://firebase.google.com/docs/firestore>
