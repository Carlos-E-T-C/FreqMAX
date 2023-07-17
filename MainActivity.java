package br.feevale.freqmax;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // Declaração dos componentes da interface do usuário
    private EditText editTextNome;
    private EditText editTextIdade;
    private AdaptadorResultado adaptadorResultado;
    private List<Atleta> listaAtletas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configuração inicial da atividade
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Inicialização dos componentes da interface do usuário
        editTextNome = findViewById(R.id.editTextNome);
        editTextIdade = findViewById(R.id.editTextIdade);
        Button buttonAdicionarECalcular = findViewById(R.id.buttonAdicionarECalcular);
        RecyclerView recyclerViewResultados = findViewById(R.id.recyclerViewResultados);
        listaAtletas = new ArrayList<>();
        adaptadorResultado = new AdaptadorResultado();

        // Configuração do RecyclerView
        recyclerViewResultados.setAdapter(adaptadorResultado);
        recyclerViewResultados.setLayoutManager(new LinearLayoutManager(this));

        // Configuração do botão "Adicionar e Calcular"
        buttonAdicionarECalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarECalcular();
            }
        });

        // Limitar o campo de nome apenas para letras e espaços
        editTextNome.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(100),
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        StringBuilder filtered = new StringBuilder(end - start);
                        for (int i = start; i < end; i++) {
                            char c = source.charAt(i);
                            if (Character.isLetter(c) || c == ' ') {
                                filtered.append(c);
                            }
                        }
                        return filtered.toString();
                    }
                }
        });

    // Configuração do botão "Adicionar e Calcular"
        buttonAdicionarECalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarECalcular();
            }
        });
    }

    // Método chamado ao clicar no botão "Adicionar e Calcular"
    private void adicionarECalcular() {
        // Obter o nome e a idade dos campos de texto
        String nome = editTextNome.getText().toString();
        String idadeTexto = editTextIdade.getText().toString();
        if (nome.isEmpty() || idadeTexto.isEmpty()) {
            Toast.makeText(MainActivity.this, "É obrigatório preencher o nome e a idade", Toast.LENGTH_SHORT).show();
            return;
        }
        int idade = Integer.parseInt(idadeTexto);
        if (idade <= 0 || idade > 150) {
            Toast.makeText(MainActivity.this, "A idade deve ser maior que 0 e menor ou igual a 150", Toast.LENGTH_SHORT).show();
            return;
        }
        int fcm = 220 - idade;
        Atleta novoAtleta = new Atleta(nome, fcm);
        listaAtletas.add(novoAtleta);
        listaAtletas.sort(new Comparator<Atleta>() {
            @Override
            public int compare(Atleta atleta1, Atleta atleta2) {
                return Integer.compare(atleta2.getFcm(), atleta1.getFcm());
            }
        });

        adaptadorResultado.setListaAtletas(listaAtletas);
        limparCamposTexto();
        editTextNome.requestFocus();
    }


    // Método para limpar os campos de texto
    private void limparCamposTexto() {
        editTextNome.setText("");
        editTextIdade.setText("");
    }

    // Classe interna para o adaptador do RecyclerView
    private class AdaptadorResultado extends RecyclerView.Adapter<AdaptadorResultado.ViewHolderResultado> {
        private List<Atleta> listaAtletas;

        @SuppressLint("NotifyDataSetChanged")
        public void setListaAtletas(List<Atleta> listaAtletas) {
            this.listaAtletas = listaAtletas;
            notifyDataSetChanged();
        }

        // Métodos obrigatórios do RecyclerView.Adapter

        @Override
        public ViewHolderResultado onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflar o layout de item_resultado para criar as visualizações
            View view = getLayoutInflater().inflate(R.layout.item_result, parent, false);
            return new ViewHolderResultado(view);
        }

        @Override
        public void onBindViewHolder(ViewHolderResultado holder, int position) {
            // Configurar as visualizações com os dados do atleta correspondente
            Atleta atleta = listaAtletas.get(position);
            holder.textViewNome.setText(atleta.getNome());
            holder.textViewFcm.setText("FCM: " + atleta.getFcm() + " bpm");
        }

        @Override
        public int getItemCount() {
            // Retornar o número de itens na lista de atletas
            return listaAtletas != null ? listaAtletas.size() : 0;
        }

        // Classe interna para o ViewHolder do RecyclerView
        public class ViewHolderResultado extends RecyclerView.ViewHolder {
            public TextView textViewNome;
            public TextView textViewFcm;

            public ViewHolderResultado(View itemView) {
                super(itemView);
                // Obter as referências das visualizações a partir do layout de item_resultado
                textViewNome = itemView.findViewById(R.id.textViewNome);
                textViewFcm = itemView.findViewById(R.id.textViewFcm);
            }
        }
    }

    // Classe interna para representar um atleta
    private static class Atleta {
        private String nome;
        private int fcm;

        public Atleta(String nome, int fcm) {
            this.nome = nome;
            this.fcm = fcm;
        }

        public String getNome() {
            return nome;
        }

        public int getFcm() {
            return fcm;
        }
    }
}
