package vista;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import modelo.Contactos;

public class Principal extends javax.swing.JFrame {

    ArrayList<Character> letrasNif = new ArrayList<>(); // ArrayList de letras para usar el metodo .contains() y facilitar la comprobacion de NIF
    ArrayList<Contactos> contacto = new ArrayList<>(); //Mi array de contactos
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Estado e;
    int avance; //Variable para moverme por la agenda
    int count; // Variable que uso para, si hay contactos creados por codigo como los de prueba, 
    //comprobar si es la primera vez que se muestran,
    //porque al usar avance ocurrian conflictos y fue mi forma de solucionarlo
    boolean respuesta; //Boleana que uso para guardar el resultado de ciertos metodos de comprobaciones
    Contactos c;

    public Principal() {
        initComponents();
        añadeLetrasArrayList();
        toolTipGenerator();
        this.setLocationRelativeTo(null); //Por comodidad hago que la ventana aparezca en el centro
        sdf.setLenient(false);
        count = 0;
        //Contactos de prueba
        /*Contactos c1 = new Contactos("12345678A", "Pedro", "Pedrin", "Pedron", 981137131, new Date(98, 8, 7), "Amigo");/*
        Contactos c2 = new Contactos("12345678B", "Adrian", "Rodrigo", "Pelotas", 981137131, new Date(99, 2, 7), "Enemigo");
        Contactos c3 = new Contactos("12345678C", "Pichu", "Licha", "Castro", 981137131, new Date(97, 0, 7), "Amigo");
        Contactos c4 = new Contactos("12345678D", "Me", "Cagoen", "Laputa", 981137131, new Date(90, 4, 7), "Trabajo");
        contacto.add(c1);
        contacto.add(c2);
        contacto.add(c3);*/
        //contacto.add(c1);

        if (contacto.isEmpty()) {
            e = Estado.ANHADIENDO;
        } else {
            e = Estado.NAVEGANDO;
            avance = 0;
        }
        seleccionEstado(e);

    }

    private void borraDeLista() {
        if (contacto.get(avance).getNif().equals(txtNif.getText())) {
            contacto.remove(avance);
            avance = 0;
            count = 0;
        }

        if (contacto.isEmpty()) {
            e = Estado.ANHADIENDO;
            anhadir();
        } else {
            navegar();
        }

    }

    private void editarContacto() throws ParseException, NumberFormatException {
        c = contacto.get(avance);
        c.setNombre(txtNombre.getText());
        c.setApellido1(txtApellido1.getText());
        c.setApellido2(txtApellido2.getText());
        c.setTelefono(Integer.parseInt(txtTelefono.getText()));
        c.setNacimiento(sdf.parse(txtNacimiento.getText()));
        c.setTipo(cmboTipo.getSelectedItem().toString());
        contacto.set(avance, c);
        navegar();
    }

    private void toolTipGenerator() {
        btnAutomatico.setToolTipText("Al pulsar este boton habiendo escrito los 8 digitos del NIF, se le pondrá automaticamente la letra correspondiente");
        txtNacimiento.setToolTipText("El formato de fecha es dd/mm/aaaa");
    }

    enum Estado {
        NAVEGANDO,
        ANHADIENDO,
        EDITANDO,
        BORRANDO
    }

    private void seleccionEstado(Estado e) {
        switch (e) {
            case ANHADIENDO:
                anhadir();
                break;
            case BORRANDO:
                borrar();
                break;
            case EDITANDO:
                editar();
                break;
            case NAVEGANDO:
                navegar();
                break;
            default:
                System.out.println("No hay seleccionado ningun estado\n");
        }
    }

    private void borrar() {
        btnAnterior.setEnabled(false);
        btnAceptar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnBorrar.setEnabled(false);
        btnEditar.setEnabled(false);
        btnPrimero.setEnabled(false);
        btnSiguiente.setEnabled(false);
        btnUltimo.setEnabled(false);
        btnAnadir.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCargar.setEnabled(false);

        txtNif.setEditable(false);
        txtNombre.setEditable(false);
        txtApellido1.setEditable(false);
        txtApellido2.setEditable(false);
        txtTelefono.setEditable(false);
        txtNacimiento.setEditable(false);
        cmboTipo.setEditable(false);

    }

    private void anhadir() {

        if (contacto.isEmpty()) {
            txtNif.setText("");
            txtNombre.setText("");
            txtApellido1.setText("");
            txtApellido2.setText("");
            txtTelefono.setText("");
            txtNacimiento.setText("");
            String[] s = {"Añadir", "Cargar", "Salir"};
            int respuesta = JOptionPane.showOptionDialog(null, "Lista vacia,¿que desea hacer?", "Aviso", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, s, s[0]);

            switch (respuesta) {
                case -1:
                    System.exit(0);
                case 0:
                    break;
                case 1:
                    try {
                        cargarDatos();
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return;
                default:
                    System.exit(0);
            }

            cmboTipo.setSelectedIndex(-1);

            btnAnterior.setEnabled(false);
            btnAceptar.setEnabled(true);
            btnCancelar.setEnabled(false);
            btnBorrar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnPrimero.setEnabled(false);
            btnSiguiente.setEnabled(false);
            btnUltimo.setEnabled(false);
            btnAnadir.setEnabled(false);
            btnGuardar.setEnabled(false);
            btnCargar.setEnabled(true);
            cmboTipo.setEnabled(true);

            txtNif.setText("");
            txtNombre.setText("");
            txtApellido1.setText("");
            txtApellido2.setText("");
            txtTelefono.setText("");
            txtNacimiento.setText("");

            txtNif.setEditable(true);
            txtNombre.setEditable(true);
            txtApellido1.setEditable(true);
            txtApellido2.setEditable(true);
            txtTelefono.setEditable(true);
            txtNacimiento.setEditable(true);

        } else {

            cmboTipo.setSelectedIndex(-1);

            btnAnterior.setEnabled(false);
            btnAceptar.setEnabled(true);
            btnCancelar.setEnabled(true);
            btnBorrar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnPrimero.setEnabled(false);
            btnSiguiente.setEnabled(false);
            btnUltimo.setEnabled(false);
            btnAnadir.setEnabled(false);
            cmboTipo.setEnabled(true);
            btnGuardar.setEnabled(false);
            btnCargar.setEnabled(false);

            txtNif.setText("");
            txtNombre.setText("");
            txtApellido1.setText("");
            txtApellido2.setText("");
            txtTelefono.setText("");
            txtNacimiento.setText("");

            txtNif.setEditable(true);
            txtNombre.setEditable(true);
            txtApellido1.setEditable(true);
            txtApellido2.setEditable(true);
            txtTelefono.setEditable(true);
            txtNacimiento.setEditable(true);
        }
    }

    private void navegar() {
        if (contacto.isEmpty()) {

            txtNif.setText("");
            txtNombre.setText("");
            txtApellido1.setText("");
            txtApellido2.setText("");
            txtTelefono.setText("");
            txtNacimiento.setText("");

            e = Estado.ANHADIENDO;
            seleccionEstado(e);
        }
        btnAnterior.setEnabled(true);
        btnAceptar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnBorrar.setEnabled(true);
        btnEditar.setEnabled(true);
        btnPrimero.setEnabled(true);
        btnSiguiente.setEnabled(true);
        btnUltimo.setEnabled(true);
        btnAnadir.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnCargar.setEnabled(true);

        txtNif.setEnabled(true);
        txtNif.setEditable(false);
        txtNombre.setEditable(false);
        txtApellido1.setEditable(false);
        txtApellido2.setEditable(false);
        txtTelefono.setEditable(false);
        txtNacimiento.setEditable(false);

        cmboTipo.setEnabled(false);
        if (count == 0) {
            Contactos c = contacto.get(0);
            txtNif.setText(c.getNif().toUpperCase());
            txtNombre.setText(c.getNombre());
            txtApellido1.setText(c.getApellido1());
            txtApellido2.setText(c.getApellido2());
            txtTelefono.setText(String.valueOf(c.getTelefono()));
            txtNacimiento.setText(sdf.format(c.getNacimiento()));
            cmboTipo.setSelectedItem(c.getTipo());
        }
        count++;
        if (avance == 0) {
            btnAnterior.setEnabled(false);
            btnPrimero.setEnabled(false);
        } else if (avance == contacto.size() - 1) {
            btnSiguiente.setEnabled(false);
            btnUltimo.setEnabled(false);
        }
        if (contacto.size() == 1) {
            btnCargar.setEnabled(true);
            btnGuardar.setEnabled(true);
            btnAnterior.setEnabled(false);
            btnPrimero.setEnabled(false);
            btnSiguiente.setEnabled(false);
            btnUltimo.setEnabled(false);
        }

    }

    private void rellenar(int valor) {
        Contactos contact = contacto.get(valor);
        txtNif.setText(contact.getNif().toUpperCase());
        txtNombre.setText(contact.getNombre());
        txtApellido1.setText(contact.getApellido1());
        txtApellido2.setText(contact.getApellido2());
        txtTelefono.setText(String.valueOf(contact.getTelefono()));
        txtNacimiento.setText(sdf.format(contact.getNacimiento()));
        cmboTipo.setSelectedItem(contact.getTipo());

    }

    private void editar() {
        btnAnterior.setEnabled(false);
        btnAceptar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnBorrar.setEnabled(false);
        btnEditar.setEnabled(false);
        btnPrimero.setEnabled(false);
        btnSiguiente.setEnabled(false);
        btnUltimo.setEnabled(false);
        btnAnadir.setEnabled(false);
        cmboTipo.setEnabled(true);

        txtNif.setEnabled(false);
        txtNif.setEditable(false);
        txtNombre.setEditable(true);
        txtApellido1.setEditable(true);
        txtApellido2.setEditable(true);
        txtTelefono.setEditable(true);
        txtNacimiento.setEditable(true);
    }

    private void anhadeLista() throws ParseException, NumberFormatException {
        if (txtNif.getText().isEmpty() || txtNombre.getText().isEmpty()
                || txtApellido1.getText().isEmpty() || txtApellido2.getText().isEmpty()
                || txtTelefono.getText().isEmpty() || txtNacimiento.getText().isEmpty()
                || cmboTipo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Alguno de los campos está vacio", "Aviso", JOptionPane.ERROR_MESSAGE);

        } else {
            if (txtTelefono.getText().length() == 9) {
                contacto.add(new Contactos(txtNif.getText(), txtNombre.getText(),
                        txtApellido1.getText(), txtApellido2.getText(), Integer.parseInt(txtTelefono.getText()),
                        sdf.parse(txtNacimiento.getText()), cmboTipo.getSelectedItem().toString()));
                e = Estado.NAVEGANDO;
                avance = 0;
                count = 0;
                seleccionEstado(e);
            } else {
                JOptionPane.showMessageDialog(null, "Telefono erroneo", "Aviso", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarDatos() throws FileNotFoundException, IOException {
        File file;
        fcGuardarCargar.showSaveDialog(null);
        file = fcGuardarCargar.getSelectedFile();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false))) {
            oos.writeObject(contacto);
        }
    }

    private boolean cargarDatos() throws IOException, ClassNotFoundException {
        count = 0;
        File file;
        int respuesta = fcGuardarCargar.showOpenDialog(null);
        file = fcGuardarCargar.getSelectedFile();
        if (JFileChooser.APPROVE_OPTION == respuesta) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {;

                contacto = (ArrayList<Contactos>) ois.readObject();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Error al cargar fichero", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Error al cargar fichero", "Error", JOptionPane.ERROR_MESSAGE);
            }
            e = Estado.NAVEGANDO;
            seleccionEstado(e);
            return true;
        } else {
            e = Estado.ANHADIENDO;
            seleccionEstado(e);
            return false;
        }
    }

    private void añadeLetrasArrayList() {

        char letrasNifs[] = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        for (int i = 0; i < letrasNifs.length; i++) {
            letrasNif.add(letrasNifs[i]);
        }
    }

    private boolean compruebaNif(String nif) throws NumberFormatException {
        if (nif.length() != 9 || Character.isDigit(nif.charAt(8)) || !letrasNif.contains(nif.charAt(8))) {
            return true;
        } else if (letrasNif.contains(nif.charAt(8))) {
            int numNif = Integer.parseInt(nif.substring(0, 8));
            int modNif = 0;

            modNif = numNif % 23;

            if (!letrasNif.get(modNif).equals(nif.charAt(8))) {
                return true;
            }

        }
        return false;

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fcGuardarCargar = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        lblNif = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblApellido1 = new javax.swing.JLabel();
        lblApellido2 = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        lblNacimiento = new javax.swing.JLabel();
        lblTipo = new javax.swing.JLabel();
        txtNif = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtApellido1 = new javax.swing.JTextField();
        txtApellido2 = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtNacimiento = new javax.swing.JTextField();
        cmboTipo = new javax.swing.JComboBox<>();
        btnAutomatico = new javax.swing.JButton();
        btnAnadir = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnPrimero = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        btnUltimo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnCargar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14))); // NOI18N

        lblNif.setText("NIF");

        lblNombre.setText("Nombre");

        lblApellido1.setText("Apellido 1");

        lblApellido2.setText("Apellido 2");

        lblTelefono.setText("Telefono");

        lblNacimiento.setText("Nacimiento");

        lblTipo.setText("Tipo");

        txtNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombreFocusLost(evt);
            }
        });

        txtApellido1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApellido1FocusLost(evt);
            }
        });

        txtApellido2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApellido2FocusLost(evt);
            }
        });

        cmboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Enemigo", "Amigo", "Trabajo", "Familiar" }));

        btnAutomatico.setText("A.C.");
        btnAutomatico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAutomaticoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNif)
                    .addComponent(lblNombre)
                    .addComponent(lblApellido1)
                    .addComponent(lblApellido2)
                    .addComponent(lblTelefono)
                    .addComponent(lblNacimiento)
                    .addComponent(lblTipo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNif, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtApellido1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtApellido2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNacimiento, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTelefono)
                    .addComponent(cmboTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addComponent(btnAutomatico)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNif)
                            .addComponent(txtNif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAutomatico))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombre)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblApellido1)
                            .addComponent(txtApellido1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblApellido2)
                            .addComponent(txtApellido2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addComponent(lblTelefono))
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNacimiento)
                    .addComponent(txtNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTipo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmboTipo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );

        btnAnadir.setText("Añadir");
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });

        btnBorrar.setText("Borrar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnPrimero.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnPrimero.setText("|<--");
        btnPrimero.setMaximumSize(new java.awt.Dimension(51, 24));
        btnPrimero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrimeroActionPerformed(evt);
            }
        });

        btnAnterior.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnAnterior.setText("<--");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });

        btnSiguiente.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnSiguiente.setText("-->");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });

        btnUltimo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnUltimo.setText("-->|");
        btnUltimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUltimoActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCargar.setText("Cargar");
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnPrimero, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAnadir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCargar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(98, 98, 98))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAceptar, btnAnadir, btnBorrar, btnCancelar, btnEditar});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(btnAnadir)
                        .addGap(48, 48, 48)
                        .addComponent(btnBorrar)
                        .addGap(48, 48, 48)
                        .addComponent(btnEditar)
                        .addGap(87, 87, 87)
                        .addComponent(btnAceptar)
                        .addGap(46, 46, 46)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSiguiente)
                    .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrimero, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCargar))
                .addContainerGap(108, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAceptar, btnAnadir, btnBorrar, btnCancelar, btnEditar});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAnterior, btnPrimero});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        switch (e) {
            case ANHADIENDO: {
                try {
                    respuesta = compruebaNif(txtNif.getText().toUpperCase());
                } catch (NumberFormatException ex) {
                    respuesta = true;
                    JOptionPane.showMessageDialog(null, "NIF con letras donde debería de haber numeros", "Error", JOptionPane.ERROR_MESSAGE);
                }

                if (respuesta) {
                    JOptionPane.showMessageDialog(null, "NIF incorrecto (Tienen que ser 8 caracteres más una letra valida)", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        anhadeLista();

                    } catch (ParseException ex) {
                        JOptionPane.showMessageDialog(null, "Formato de la fecha incorrecto (dd/MM/aaaa)", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Error en el apartado 'Telefono'", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            break;
            case BORRANDO:
                borraDeLista();
                break;
            case EDITANDO: {
                try {
                    editarContacto();
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Formato de la fecha incorrecto (dd/MM/aaaa)", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Error en el apartado 'Telefono'", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
            break;
            default:
                System.out.println("Algo raro ha pasado\n");
        }

    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        if (avance == contacto.size() - 1) {
            rellenar(avance);
        } else if (avance < contacto.size()) {
            avance++;
            rellenar(avance);
        }
        navegar();
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed
        e = Estado.ANHADIENDO;
        seleccionEstado(e);
    }//GEN-LAST:event_btnAnadirActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed

        if (avance == 0) {
            rellenar(avance);
        } else if (avance > 0) {
            avance--;
            rellenar(avance);
        }

        navegar();

    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnUltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUltimoActionPerformed
        txtNif.setText(contacto.get(contacto.size() - 1).getNif().toUpperCase());
        txtNombre.setText(contacto.get(contacto.size() - 1).getNombre());
        txtApellido1.setText(contacto.get(contacto.size() - 1).getApellido1());
        txtApellido2.setText(contacto.get(contacto.size() - 1).getApellido2());
        txtTelefono.setText(String.valueOf(contacto.get(contacto.size() - 1).getTelefono()));
        txtNacimiento.setText(sdf.format(contacto.get(contacto.size() - 1).getNacimiento()));
        cmboTipo.setSelectedItem(contacto.get(contacto.size() - 1).getTipo());
        avance = contacto.size() - 1;
        navegar();
    }//GEN-LAST:event_btnUltimoActionPerformed

    private void btnPrimeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrimeroActionPerformed
        txtNif.setText(contacto.get(0).getNif().toUpperCase());
        txtNombre.setText(contacto.get(0).getNombre());
        txtApellido1.setText(contacto.get(0).getApellido1());
        txtApellido2.setText(contacto.get(0).getApellido2());
        txtTelefono.setText(String.valueOf(contacto.get(0).getTelefono()));
        txtNacimiento.setText(sdf.format(contacto.get(0).getNacimiento()));
        cmboTipo.setSelectedItem(contacto.get(0).getTipo());
        btnAnterior.setEnabled(false);
        btnPrimero.setEnabled(false);
        avance = 0;
        navegar();


    }//GEN-LAST:event_btnPrimeroActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        e = Estado.BORRANDO;
        seleccionEstado(e);
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        count = 0;
        avance = 0;
        e = Estado.NAVEGANDO;
        seleccionEstado(e);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        e = Estado.EDITANDO;
        seleccionEstado(e);
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            guardarDatos();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed
        if (!contacto.isEmpty()) {
            String[] s = {"Guardar", "Cargar Igualmente"};

            int respuesta = JOptionPane.showOptionDialog(null, "Si cargas sin guardar perderas los contactos actuales,¿Seguro que deseas cargar?", "Aviso", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, s, s[0]);

            switch (respuesta) {
                case JOptionPane.CLOSED_OPTION: {
                    e = Estado.NAVEGANDO;
                    seleccionEstado(e);
                }
                break;
                case 0: {
                    try {
                        guardarDatos();
                        cargarDatos();
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case 1: {
                    try {
                        cargarDatos();
                    } catch (IOException ex) {
                        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, "Error al cargar fichero", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            }
        } else {

            try {
                try {
                    cargarDatos();
                } catch (FileNotFoundException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cargar fichero", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnCargarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        String[] s = {"Guardar", "Salir Sin Guardar"};

        int respuesta = JOptionPane.showOptionDialog(null, "Vas a salir sin guardar, ¿Estás seguro?", "Aviso", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, s, s[0]);

        switch (respuesta) {
            case JOptionPane.CLOSED_OPTION: {
                e = Estado.NAVEGANDO;
                seleccionEstado(e);
            }
            break;
            case 0: {
                try {
                    guardarDatos();
                } catch (IOException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case 1:
                break;
        }
    }//GEN-LAST:event_formWindowClosing

    private void txtNombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreFocusLost

        if (comprobarTxts(txtNombre.getText())) {
            JOptionPane.showMessageDialog(null, "'Nombre' no tiene que incluir digitos", "Error", JOptionPane.ERROR_MESSAGE);
            txtNombre.setText("");
        } else {

        }
    }//GEN-LAST:event_txtNombreFocusLost

    private void txtApellido1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellido1FocusLost
        if (comprobarTxts(txtApellido1.getText())) {
            JOptionPane.showMessageDialog(null, "'Apellido 1' no tiene que incluir digitos", "Error", JOptionPane.ERROR_MESSAGE);
            txtApellido1.setText("");
        } else {

        }
    }//GEN-LAST:event_txtApellido1FocusLost

    private void txtApellido2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellido2FocusLost
        if (comprobarTxts(txtApellido2.getText())) {
            JOptionPane.showMessageDialog(null, "'Apellido 2' no tiene que incluir digitos", "Error", JOptionPane.ERROR_MESSAGE);
            txtApellido2.setText("");
        } else {

        }
    }//GEN-LAST:event_txtApellido2FocusLost

    private void btnAutomaticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAutomaticoActionPerformed
        String nif = txtNif.getText();
        if (nif.length() == 8) {
            nif = nif.substring(0, 8);
            int calc = Integer.parseInt(nif) % 23;
            char letra = letrasNif.get(calc);
            txtNif.setText(txtNif.getText() + "" + letra);

        } else if (nif.length() > 8) {
            if (Character.isLetter(nif.charAt(8))) {

            } else {
                JOptionPane.showMessageDialog(null, "Tamaño de NIF incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (nif.length() < 8) {
            JOptionPane.showMessageDialog(null, "Tamaño de NIF incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAutomaticoActionPerformed

    private boolean comprobarTxts(String txt) {

        for (int i = 0; i < txt.length(); i++) {

            if (Character.isDigit(txt.charAt(i))) {
                return true;
            } else {

            }
        }
        return false;
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {

            new Principal().setVisible(true);

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnAnadir;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnAutomatico;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnPrimero;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JButton btnUltimo;
    private javax.swing.JComboBox<String> cmboTipo;
    private javax.swing.JFileChooser fcGuardarCargar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblApellido1;
    private javax.swing.JLabel lblApellido2;
    private javax.swing.JLabel lblNacimiento;
    private javax.swing.JLabel lblNif;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JTextField txtApellido1;
    private javax.swing.JTextField txtApellido2;
    private javax.swing.JTextField txtNacimiento;
    private javax.swing.JTextField txtNif;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
