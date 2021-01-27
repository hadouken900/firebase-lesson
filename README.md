# Firebase

## Часть 1. Firebase Firestore
![a](https://www.cybersecurity-help.cz/upload/iblock/ae2/ae2972c53a77d5b290fdb8ac6830495a.png)



Firebase Firestore - это база данных, которая изменяется в реальном времени и хранит данные в формате JSON.
Любые изменения тут же синхронизируются между всеми клиентами, или девайсами, которые используют одну и ту же базу данных.
Другими словами, обновление в Firebase происходят мгновенно.

Полезные ссылки:
* [Google Firebase]
* [Firestore Documentation]

### Теория

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
### Подготовка:
- войти в гугл аккаунт на firebase.google.com
- перейти в консоль 
- создать проект
- подключить приложение к проекту
- создать firebase firestore в тестовом режиме
- скопировать файлы из этого репозитория в ваш проект (файлы java в папку java/ваша_папка/ , файлы xml в res/layout/)
- поменять строчку package в каждом java файле

### Задания:
1. добавление сообщения в firestore
2. добавление SnapshotListener для автоматического обновления списка сообщений
3. удаление сообщения из firestore
4. отсортировать сообщения на экране по дате
5. (дополнительно) изменить макеты


## Часть 2. Firebase Authentication

Помимо хранения данных, в Firebase есть встроенный сервис аутентификации пользователей. Говорить о важности данной возможности, думаю, бессмысленно. Любое современное приложение имеет функцию добавления пользователей. Это позволяет разделять доступ, хранить личные данные в облаке, подстраивать функционал приложения под конкретного пользователя и т.д.
Firebase Authentication позволяет выбрать разные способы аутентификации - по электронной почте и паролю, аккаунту в Facebook, Twitter или Google. 

* [Firebase Authentication Documentation]

### Теория 
После подключения authentication к проекту, в нужной активности нужно добавить поле 
```java
private FirebaseAuth mAuth;
```
В методе onCreate иницаилизируем это поле
```java
mAuth = FirebaseAuth.getInstance();
```

Для регистрации пользователя через емейл и пароль
```java
mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                
                    // Успешная регистрация, можем получить пользователя и продолжить работу
                    FirebaseUser user = mAuth.getCurrentUser();
                    
                } else {
                
                    // Если что то пошло не так, то выводим toast
                    Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                   
                }

            }
        });
```

Для входа
```java
mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    
                    // Успешная вход, можем получить пользователя и продолжить работу
                    FirebaseUser user = mAuth.getCurrentUser();
                    
                } else {
                
                    // Если что то пошло не так, то выводим toast
                    Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                   
                  
                }

           
            }
        });
```


После запуска активности в методе OnStart можно проверить текущего пользователя 

```java
@Override
public void onStart() {
    super.onStart();
    
    FirebaseUser currentUser = mAuth.getCurrentUser();
    
}
```

### Подготовка:
- подключить firebase authenticate в android studio
- на сайте во вкладке authenticate выбрать метод для входа "Адрес электронной почти и пароль"

### Задания:
1. добавить возможность авторизации в приложение
2. создать экран входа, который будет переносить пользователя в MainActivity после успешной авторизации
3. создать экран для регистрации пользователей
4. изменить макеты и классы так, чтобы при отправке сообщений было видно пользователя кто это сделал

## Часть 3. Firebase Storage

Для хранения больших файлов, таких как изображения, музыка, видео, используется Firebase Storage.

После подключения зависимостей, можно объявить такие поля:
```java
FirebaseStorage storage = FirebaseStorage.getInstance();
StorageReference storageRef = storage.getReference();
```
Когда вам понадобится сохранить файл, в первую очередь нужно позаботиться о создании уникального имени, иначе данные будут перезаписываться. Например:
```java
//добавляем путь по которому будет лежать файл в хранилище
StorageReference ref = storageRef.child("images").child("img_1");   //     ../images/img_1

//сохраняем файл в хранилище
ref.putFile(URI);

//получаем созданный Uri
ref.getDownloadUrl().getResult();
```
Где взять URI?

Допустим у вас на макете есть кнопка с методом addImage и пустой ImageView
```java

//достаем картинку из галереи с помощью интента
public void addImage(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //указываем тип. вместо звездочки можно поставить например png или jpg. звездочка означает все типы картинок
        intent.setType("image/*");
        
        //запускаем intent. второй параметр это просто целочисленная константа
        startActivityForResult(intent, GET_IMAGE);
        
        //далее открывается галерея, оттуда выбираем картинку
    }

//если всё ок, то достаем URI из полученой data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
            
                //вот этот Uri можно сохранить в хранилище
                Uri imageUri = data.getData();
                
                //в imageView вставляем картинку по URI
                image.setImageURI(imageUri);
            }
        }
    }
```

### Подготовка:
- подключить firebase storage в android studio
- для второго задания необходимо в сборщик gradle добавить зависимость:

``` implementation 'com.squareup.picasso:picasso:2.71828' ```

### Задания:
1. сохранить картинку в хранилище вместе с сообщением
2. изменить макет и адаптер, чтобы если у сообщения есть прикрепленная картинка, то она отображалась в списке (потребуется библиотека [Picasso] для загрузки картинок из интернета)
3. при нажатии на элемент списка должна открываться новая активность со всеми данными из сообщения и картинкой в полном размере
4. при удалении сообщения, должна удаляться и картинка из storage
5. провести рефакторинг




  [Google Firebase]: <https://firebase.google.com>
  [Firestore Documentation]: <https://firebase.google.com/docs/firestore>
  [Firebase Authentication Documentation]: <https://firebase.google.com/docs/auth>
  [Picasso]: <https://square.github.io/picasso/>
