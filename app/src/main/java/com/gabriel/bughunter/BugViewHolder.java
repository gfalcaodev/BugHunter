package com.gabriel.bughunter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.bughunter.databinding.ItemBugBinding;

public class BugViewHolder extends RecyclerView.ViewHolder {

    private final ItemBugBinding binding;

    public BugViewHolder(@NonNull ItemBugBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Bug bug,
                     BugAdapter.OnBugClickListener clickListener,
                     BugAdapter.OnBugLongClickListener longClickListener) {

        Context context = binding.getRoot().getContext();

        // Textos: todos são atualizados em todo bind
        binding.textCodigo.setText(bug.getCodigo());
        binding.textTitulo.setText(bug.getTitulo());
        binding.textModulo.setText(context.getString(R.string.item_modulo, bug.getModulo()));
        binding.textDescricao.setText(bug.getDescricao());

        // Imagem
        binding.imageBug.setImageResource(bug.getImagemResId());

        // Gravidade: texto e cor
        int gravidade = bug.getGravidade();
        binding.textGravidade.setText(context.getString(
                R.string.item_gravidade,
                gravidade,
                context.getString(obterNomeGravidade(gravidade))));
        binding.textGravidade.setBackgroundTintList(
                ContextCompat.getColorStateList(context, obterCorGravidade(gravidade)));

        // Status: Aberto ou Corrigido (texto, cor da etiqueta e cor do cartão)
        if (bug.isCorrigido()) {
            binding.textStatus.setText(R.string.status_corrigido);
            binding.textStatus.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.status_corrigido));
            binding.getRoot().setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.card_corrigido));
        } else {
            binding.textStatus.setText(R.string.status_aberto);
            binding.textStatus.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.status_aberto));
            binding.getRoot().setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.card_aberto));
        }

        // Eventos: encaminham o objeto Bug, não a posição
        binding.getRoot().setOnClickListener(view -> clickListener.onBugClick(bug));

        binding.getRoot().setOnLongClickListener(view -> {
            longClickListener.onBugLongClick(bug);
            return true;
        });
    }

    public static int obterNomeGravidade(int gravidade) {
        if (gravidade <= 2) {
            return R.string.gravidade_baixa;
        } else if (gravidade == 3) {
            return R.string.gravidade_media;
        } else {
            return R.string.gravidade_alta;
        }
    }

    public static int obterCorGravidade(int gravidade) {
        if (gravidade <= 2) {
            return R.color.gravidade_baixa;
        } else if (gravidade == 3) {
            return R.color.gravidade_media;
        } else {
            return R.color.gravidade_alta;
        }
    }
}
