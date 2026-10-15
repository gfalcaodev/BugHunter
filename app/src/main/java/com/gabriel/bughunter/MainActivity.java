package com.gabriel.bughunter;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.bughunter.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PONTOS_POR_GRAVIDADE = 10;
    private static final int TAMANHO_MINIMO_DESCRICAO = 10;

    private ActivityMainBinding binding;
    private final ArrayList<Bug> bugs = new ArrayList<>();
    private BugAdapter adapter;
    private int proximoNumero = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        configurarRecycler();
        configurarSeekBar();

        binding.buttonRegistrar.setOnClickListener(view -> registrarBug());

        atualizarPainel();
    }

    private void configurarRecycler() {
        adapter = new BugAdapter(
                bugs,
                bug -> alternarStatus(bug),
                bug -> removerBug(bug)
        );
        binding.recyclerBugs.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerBugs.setAdapter(adapter);
    }

    private void configurarSeekBar() {
        atualizarTextoGravidade(binding.seekGravidade.getProgress());

        binding.seekGravidade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                atualizarTextoGravidade(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void atualizarTextoGravidade(int gravidade) {
        binding.textGravidade.setText(getString(
                R.string.label_gravidade,
                gravidade,
                getString(BugViewHolder.obterNomeGravidade(gravidade))));
        binding.textGravidade.setTextColor(
                ContextCompat.getColor(this, BugViewHolder.obterCorGravidade(gravidade)));
    }

    private void registrarBug() {
        String titulo = binding.editTitulo.getText().toString().trim();
        String modulo = binding.editModulo.getText().toString().trim();
        String descricao = binding.editDescricao.getText().toString().trim();

        if (titulo.isEmpty()) {
            binding.editTitulo.setError(getString(R.string.erro_titulo));
            binding.editTitulo.requestFocus();
            return;
        }

        if (modulo.isEmpty()) {
            binding.editModulo.setError(getString(R.string.erro_modulo));
            binding.editModulo.requestFocus();
            return;
        }

        if (descricao.length() < TAMANHO_MINIMO_DESCRICAO) {
            binding.editDescricao.setError(getString(R.string.erro_descricao));
            binding.editDescricao.requestFocus();
            return;
        }

        int gravidade = binding.seekGravidade.getProgress();
        String codigo = getString(R.string.formato_codigo, proximoNumero);
        proximoNumero++;

        // 1. Criar o novo objeto
        Bug novoBug = new Bug(codigo, titulo, modulo, descricao,
                gravidade, obterImagemDaGravidade(gravidade));

        // 2. Adicionar ao final do ArrayList
        bugs.add(novoBug);
        int posicao = bugs.size() - 1;

        // 3. Atualizar somente o novo item
        adapter.notifyItemInserted(posicao);

        // 4. Rolar até o registro inserido
        binding.recyclerBugs.scrollToPosition(posicao);

        // 5. Limpar o formulário e 6. atualizar contadores
        limparFormulario();
        atualizarPainel();

        Toast.makeText(this, getString(R.string.msg_bug_registrado, codigo),
                Toast.LENGTH_SHORT).show();
    }

    private void alternarStatus(Bug bug) {
        int posicao = bugs.indexOf(bug);
        if (posicao == RecyclerView.NO_POSITION) {
            return;
        }

        bug.setCorrigido(!bug.isCorrigido());
        adapter.notifyItemChanged(posicao);
        atualizarPainel();
    }

    private void removerBug(Bug bug) {
        int posicao = bugs.indexOf(bug);
        if (posicao == RecyclerView.NO_POSITION) {
            return;
        }

        bugs.remove(posicao);
        adapter.notifyItemRemoved(posicao);
        atualizarPainel();

        Toast.makeText(this, getString(R.string.msg_bug_removido, bug.getCodigo()),
                Toast.LENGTH_SHORT).show();
    }

    private void atualizarPainel() {
        int total = bugs.size();
        int corrigidos = 0;
        int pontuacao = 0;

        for (Bug bug : bugs) {
            if (bug.isCorrigido()) {
                corrigidos++;
                pontuacao += bug.getGravidade() * PONTOS_POR_GRAVIDADE;
            }
        }

        int abertos = total - corrigidos;

        binding.textTotal.setText(getString(R.string.painel_total, total));
        binding.textAbertos.setText(getString(R.string.painel_abertos, abertos));
        binding.textCorrigidos.setText(getString(R.string.painel_corrigidos, corrigidos));
        binding.textPontuacao.setText(getString(R.string.painel_pontuacao, pontuacao));

        if (bugs.isEmpty()) {
            binding.textListaVazia.setVisibility(View.VISIBLE);
        } else {
            binding.textListaVazia.setVisibility(View.GONE);
        }
    }

    private void limparFormulario() {
        binding.editTitulo.setText("");
        binding.editModulo.setText("");
        binding.editDescricao.setText("");
        binding.seekGravidade.setProgress(1);
        binding.editTitulo.requestFocus();
    }

    private int obterImagemDaGravidade(int gravidade) {
        if (gravidade <= 2) {
            return R.drawable.ic_bug_baixa;
        } else if (gravidade == 3) {
            return R.drawable.ic_bug_media;
        } else {
            return R.drawable.ic_bug_alta;
        }
    }
}
