    <main class="container pt-5">
        <div class="card mb-3">
            <div class="card-header"><?php echo $title ?></div>
            <div class="card-block p-0">               
                <?php if(isset($error)) {
                    echo "<p class = \"text-warning p-3\">$error</p>";
                } else {
                ?>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover table-sm m-0 text-nowrap">
                        <thead class="">
                            <tr>
                                <th>Benutzer</th>
                                <?php foreach ($headings as $key => $value): ?>
                                <th><?php echo $value; ?></th>
                                <?php endforeach; ?>
                                <th>Funktionen</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php foreach ($rows as $username => $values): ?>
                            <tr<?php if( isset($markedid) && $values['iduser'] == $markedid ) {echo " class=\"table-success\"";} ?>>
                                <td><?php echo $username; ?></td>
                                <?php foreach ($values['teams'] as $teamname => $attribute): ?>
                                <td>
                                    <div class="custom-control custom-checkbox">
                                        <input type="checkbox" class="custom-control-input" <?php if ( $attribute['value'] == 1 ) { echo 'checked="checked"'; } ?> >
                                        <label class="custom-control-label" for=""></label>
                                    </div>
                                </td>
                                <?php endforeach; ?>
                                <td>
                                    <a href="<?php echo base_url('/UserTeamMappingEdit/edit/' . $values['iduser']); ?>" class="btn btn-secondary">
                                        <span class="fa fa-pencil-square-o"></span>
                                    </a>
                                </td>
                            </tr>
                            <?php endforeach; ?>
                        </tbody>
                    </table>
                </div>
                <?php } ?>
            </div>
            <div class="card-footer">
                <div>
                    <a href="<?php echo base_url('/UserTeamMappingAdd/'); ?>" class="btn btn-primary">
                        <span class="fa fa-plus-square-o"></span> Neuen Benutzer berechtigen
                    </a>
                </div>
            </div>
        </div>
    </main>