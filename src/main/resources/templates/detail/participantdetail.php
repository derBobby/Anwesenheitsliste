    <main class="container pt-5">
        <div class="card mb-3">

            <?php echo form_open($function); ?>
            
            <div class="card-header">
                <?php echo $title ?>
            </div>
            
            <div class="card-block p-3">

                <?php
                echo validation_errors("<p class = \"text-warning\">", "</p>"); //TODO new row?

                if(isset($error) && !empty($error)) {
                    echo "<p class = \"text-warning\">$error</p>"; //TODO br?
                }

                echo form_hidden('idparticipant', isset($participant['idparticipant']) ? $participant['idparticipant'] : "");

                echo "<div class=\"form-group\">";

                echo form_label('Vorname:', 'firstname');
                echo form_input(array(
                    'id'=>'firstname',
                    'name'=>'firstname',
                    'class'=>'form-control',
                    'value'=> isset($participant['firstname']) ? $participant['firstname'] : ""));

                echo "</div>";
                echo "<div class=\"form-group\">";

                echo form_label('Nachname:', 'lastname');
                echo form_input(array(
                    'id'=>'lastname',
                    'name'=>'lastname',
                    'class'=>'form-control',
                    'value'=> isset($participant['lastname']) ? $participant['lastname'] : ""));

                echo "</div>";
                echo "<div class=\"form-group\">";

                echo form_label('E-mail:', 'email');
                echo form_input(array(
                    'id'=>'email',
                    'name'=>'email',
                    'class'=>'form-control',
                    'value'=> isset($participant['email']) ? $participant['email'] : ""));

                echo "</div>";
                echo "<div class=\"form-group\">";

                echo form_label('Telefon:', 'phone');
                echo form_input(array(
                    'id'=>'phone',
                    'name'=>'phone',
                    'class'=>'form-control',
                    'value'=> isset($participant['phone']) ? $participant['phone'] : ""));

                echo "</div>";
                echo "<div class=\"form-group\">";
                echo "<div class=\"custom-control custom-checkbox\">";

                echo form_checkbox(array(
                    'id'=>'isactive',
                    'name'=>'isactive',
                    'class' => "custom-control-input",
                    'checked'=> isset($participant['isactive']) && $participant['isactive'] == 1 ? "checked" : ""));
                echo form_label('Aktiv', 'isactive', "class=\"custom-control-label\"");

                echo "</div>";
                echo "</div>";
                ?>
                
            </div>

            <div class="card-footer">
                
                <div class="btn-group">
                    <?php
                    echo form_submit(array(
                        'id'=>'submit',
                        'value'=>'Speichern',
                        'class'=>'btn btn-primary')); 
                    ?>
                    <a href="<?php echo base_url('/ParticipantList'); ?>" class="btn btn-secondary">
                        <i class="fa fa-times"></i> 
                    </a>
                </div>
                
            </div>
            
            <?php echo form_close(); ?>
        </div>
    </main>
