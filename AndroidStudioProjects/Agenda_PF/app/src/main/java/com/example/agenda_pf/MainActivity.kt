package com.example.agenda_pf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agenda_pf.ui.theme.Agenda_PFTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Agenda_PFTheme {
                val navController = rememberNavController()
                Navigation(navController)
            }
        }
    }
}

// Almacenamos las listas de notas y tareas
val notesList = mutableStateListOf<Note>()
val tasksList = mutableStateListOf<Task>()

data class Note(
    val title: String,
    val description: String,
    val multimedia: List<Multimedia> = emptyList() // Lista de archivos multimedia
)

data class Multimedia(
    val filePath: String, // Ruta o URI del archivo multimedia
    val description: String // Descripción del archivo
)

data class Task(
    val title: String,
    val description: String,
    val dueDate: Date,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) } // Para mostrar el diálogo

    if (showDialog) {
        // Diálogo para seleccionar entre nota o tarea
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Agregar Nota o Tarea") },
            text = { Text("Selecciona una opción para agregar:") },
            confirmButton = {
                TextButton(
                    onClick = {
                        navController.navigate("addNote")
                        showDialog = false
                    }
                ) {
                    Text("Agregar Nota")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        navController.navigate("addTask")
                        showDialog = false
                    }
                ) {
                    Text("Agregar Tarea")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Agenda",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE1BEE7)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }, // Mostrar el diálogo al presionar +
                containerColor = Color(0xFF4CAF50),
                content = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_input_add),
                        contentDescription = "Agregar",
                        tint = Color.White
                    )
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8BBD0))
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_notas),
                contentDescription = "Notas",
                modifier = Modifier
                    .size(190.dp)
                    .clickable { navController.navigate("notesList") }
            )
            Text(
                text = "Notas",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_tareas),
                contentDescription = "Tareas",
                modifier = Modifier
                    .size(190.dp)
                    .clickable { navController.navigate("tasksList") }
            )
            Text(
                text = "Tareas",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AddNoteScreen(navController: NavHostController, isTask: Boolean) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var multimediaList by remember { mutableStateOf(mutableListOf<Multimedia>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text(text = "Título") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text(text = "Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!isTask) {
            // Sección para agregar multimedia solo si es una nota
            Text(text = "Archivos Multimedia Adjuntos:", fontWeight = FontWeight.Bold)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(multimediaList) { multimedia ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = "Multimedia",
                            modifier = Modifier.size(50.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = multimedia.filePath, fontWeight = FontWeight.Bold)
                            Text(text = multimedia.description)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    multimediaList.add(
                        Multimedia(
                            filePath = "Ruta/Del/Archivo", // Simulación de ruta del archivo
                            description = "Descripción del archivo"
                        )
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Agregar Archivo Multimedia")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                if (isTask) {
                    tasksList.add(Task(title.text, description.text, Calendar.getInstance().time))
                    navController.navigate("tasksList")
                } else {
                    notesList.add(Note(title.text, description.text, multimediaList))
                    navController.navigate("notesList")
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = if (isTask) "Guardar Tarea" else "Guardar Nota")
        }
    }
}

@Composable
fun NotesListScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Filtramos las notas según el texto de búsqueda
    val filteredNotes = if (searchQuery.text.isEmpty()) {
        notesList
    } else {
        notesList.filter {
            it.title.contains(searchQuery.text, ignoreCase = true) ||
                    it.description.contains(searchQuery.text, ignoreCase = true)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addNote") },
                containerColor = Color(0xFF4CAF50),
                content = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_input_add),
                        contentDescription = "Agregar Nota",
                        tint = Color.White
                    )
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                TextButton(
                    onClick = { navController.navigate("main") },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text(text = "Regresar", fontSize = 16.sp, color = Color.Blue)
                }

                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(text = "Buscar Notas") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredNotes.size) { index ->
                        val note = filteredNotes[index]
                        NoteItem(note.title, note.description, onEdit = {
                            navController.navigate("editNote/$index")
                        }, onDelete = {
                            notesList.removeAt(index)
                        })
                    }
                }
            }
        }
    )
}

@Composable
fun TasksListScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Filtramos las tareas según el texto de búsqueda
    val filteredTasks = if (searchQuery.text.isEmpty()) {
        tasksList
    } else {
        tasksList.filter {
            it.title.contains(searchQuery.text, ignoreCase = true) ||
                    it.description.contains(searchQuery.text, ignoreCase = true)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addTask") },
                containerColor = Color(0xFF4CAF50),
                content = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_input_add),
                        contentDescription = "Agregar Tarea",
                        tint = Color.White
                    )
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                TextButton(
                    onClick = { navController.navigate("main") },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text(text = "Regresar", fontSize = 16.sp, color = Color.Blue)
                }

                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(text = "Buscar Tareas") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredTasks.size) { index ->
                        val task = filteredTasks[index]
                        TaskItem(task.title, task.description, onEdit = {
                            navController.navigate("editTask/$index")
                        }, onDelete = {
                            tasksList.removeAt(index)
                        })
                    }
                }
            }
        }
    )
}

@Composable
fun NoteItem(nota: String, descripcion: String, onEdit: () -> Unit, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = nota, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = descripcion, fontSize = 14.sp, color = Color.Gray)
            }
            // IconButton para el menú de tres puntos dentro de un Box para evitar movimientos
            Box {
                IconButton(onClick = { expanded = !expanded }) {
                    Image(
                        painter = painterResource(id = R.drawable.icono_tres_puntos), // Reemplaza con el ID de tu icono
                        contentDescription = "Opciones"
                    )
                }
                // DropdownMenu que no afecta el layout al expandirse
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.wrapContentSize()
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            expanded = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun TaskItem(tarea: String, descripcion: String, onEdit: () -> Unit, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = tarea, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = descripcion, fontSize = 14.sp, color = Color.Gray)
            }
            // IconButton para el menú de tres puntos dentro de un Box para evitar movimientos
            Box {
                IconButton(onClick = { expanded = !expanded }) {
                    Image(
                        painter = painterResource(id = R.drawable.icono_tres_puntos), // Reemplaza con el ID de tu icono
                        contentDescription = "Opciones"
                    )
                }
                // DropdownMenu que no afecta el layout al expandirse
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.wrapContentSize()
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            expanded = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("notesList") { NotesListScreen(navController) }
        composable("tasksList") { TasksListScreen(navController) }
        composable("addNote") { AddNoteScreen(navController, isTask = false) }
        composable("addTask") { AddNoteScreen(navController, isTask = true) }
        composable("editNote/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull()
            index?.let { EditNoteScreen(navController, it) }
        }
        composable("editTask/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull()
            index?.let { EditTaskScreen(navController, it) }
        }
    }
}

@Composable
fun EditNoteScreen(navController: NavHostController, index: Int) {
    var title by remember { mutableStateOf(TextFieldValue(notesList[index].title)) }
    var description by remember { mutableStateOf(TextFieldValue(notesList[index].description)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text(text = "Título") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text(text = "Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                notesList[index] = notesList[index].copy(title = title.text, description = description.text)
                navController.navigate("notesList")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Guardar Cambios")
        }
    }
}

@Composable
fun EditTaskScreen(navController: NavHostController, index: Int) {
    var title by remember { mutableStateOf(TextFieldValue(tasksList[index].title)) }
    var description by remember { mutableStateOf(TextFieldValue(tasksList[index].description)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text(text = "Título") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text(text = "Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                tasksList[index] = tasksList[index].copy(title = title.text, description = description.text)
                navController.navigate("tasksList")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Guardar Cambios")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    Agenda_PFTheme {
        MainScreen(navController)
    }
}
