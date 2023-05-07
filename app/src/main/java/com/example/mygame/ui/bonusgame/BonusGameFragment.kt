package com.example.mygame.ui.bonusgame

import android.animation.Animator
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mygame.databinding.FragmentBonusGameBinding
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.viewbinding.library.fragment.viewBinding
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mygame.R
import com.example.mygame.adapters.CardItem
import com.example.mygame.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.get
import timber.log.Timber


class BonusGameFragment : MvpAppCompatFragment(), BonusGameView {

    @InjectPresenter
    lateinit var presenter: BonusGamePresenter

    @ProvidePresenter
    fun provideBonusGamePresenter() = get<BonusGamePresenter>()

    private lateinit var binding: FragmentBonusGameBinding

    lateinit var front_animation: AnimatorSet
    lateinit var back_animation: AnimatorSet

    private val groupAnimator = AnimatorSet()
    private val currentAnimatorSet = mutableListOf<Animator>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = FragmentBonusGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun disableAllCardClick() {
        with(binding) {
            cardOneImageView.isClickable = false
            cardTwoImageView.isClickable = false
            cardThreeImageView.isClickable = false
            cardFourImageView.isClickable = false
        }
    }

    private fun enableAllCardClick() {
        with(binding) {
            cardOneImageView.isClickable = true
            cardTwoImageView.isClickable = true
            cardThreeImageView.isClickable = true
            cardFourImageView.isClickable = true
        }
    }

    private fun getCardList(): MutableList<ImageView> {
        with(binding) {
            return mutableListOf(
                cardOneImageView,
                cardTwoImageView,
                cardThreeImageView,
                cardFourImageView
            )
        }
    }

    private fun getCardFrontList(): MutableList<ImageView> {
        with(binding) {
            return mutableListOf(
                cardOneFrontalImageView,
                cardTwoFrontalImageView,
                cardThreeFrontalImageView,
                cardFourFrontalImageView
            )
        }
    }

    private fun setOnClickListenerOnCard(
        card: ImageView,
        frontalCard: ImageView,
        currentCardItem: CardItem,
        chosenIndex: Int
    ) {
        frontalCard.setImageDrawable(currentCardItem.image)

        card.setOnClickListener {
            if (presenter.musicVolume == 0) {
                (activity as MainActivity).vibrate(context)
            }
            var balance = presenter.getBalance()
            if (currentCardItem.win != 0) {
                presenter.playOnWin()
                balance += currentCardItem.win

            } else {
                balance -= 200
                if (balance <= 0) {
                    balance = 1000
                }
            }
            presenter.setBalance(balance)
            binding.balanceTextView.text = balance.toString()
            disableAllCardClick()
            openCard(card, frontalCard)
            lifecycleScope.launch {
                delay(1000)
                openAllCards(chosenIndex)
                delay(2000)
                closeAllCards()
                delay(1000)
                enableAllCardClick()
                setOnClickListenerOnCards()
            }
        }
    }

    private fun closeAllCards() {
        currentAnimatorSet.clear()
        for (i in (0..3)) {
            val f_a = AnimatorInflater.loadAnimator(
                requireActivity().applicationContext,
                R.animator.front_animator
            ) as AnimatorSet
            val f_b = AnimatorInflater.loadAnimator(
                requireActivity().applicationContext,
                R.animator.back_animator
            ) as AnimatorSet
            f_a.setTarget(getCardFrontList()[i])
            f_b.setTarget(getCardList()[i])
            currentAnimatorSet.add(f_a)
            currentAnimatorSet.add(f_b)
        }
        groupAnimator.playTogether(currentAnimatorSet)
        for (animator in currentAnimatorSet) {
            animator.start()
        }
    }

    private fun openAllCards(chosenIndex: Int) {
        currentAnimatorSet.clear()
        for (i in (0..3)) {
            if (i != chosenIndex) {
                val f_a = AnimatorInflater.loadAnimator(
                    requireActivity().applicationContext,
                    R.animator.front_animator
                ) as AnimatorSet
                val f_b = AnimatorInflater.loadAnimator(
                    requireActivity().applicationContext,
                    R.animator.back_animator
                ) as AnimatorSet
                f_a.setTarget(getCardList()[i])
                f_b.setTarget(getCardFrontList()[i])
                currentAnimatorSet.add(f_a)
                currentAnimatorSet.add(f_b)
            }
        }
        groupAnimator.playTogether(currentAnimatorSet)
        for (animator in currentAnimatorSet) {
            animator.start()
        }
    }

    private fun openCard(
        card: ImageView,
        frontalCard: ImageView
    ) {
        front_animation.setTarget(card)
        back_animation.setTarget(frontalCard)
        back_animation.start()
        front_animation.start()
    }

    private fun getBonusCards(): MutableList<CardItem> {
        val bonusListDrawable = mutableListOf<CardItem>()
        bonusListDrawable.add(
            CardItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources, R.drawable.bonus_game_card1, requireContext().theme
                ), 600
            )
        )
        bonusListDrawable.add(
            CardItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources, R.drawable.bonus_game_card2, requireContext().theme
                ), 400
            )
        )
        bonusListDrawable.add(
            CardItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources, R.drawable.bonus_game_card3, requireContext().theme
                ), 200
            )
        )
        bonusListDrawable.add(
            CardItem(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.bonus_game_card_empty,
                    requireContext().theme
                ), 0
            )
        )
        return bonusListDrawable
    }

    private fun getEmptyCard(): Drawable? {
        return ResourcesCompat.getDrawable(
            requireContext().resources,
            R.drawable.bonus_game_card_empty,
            requireContext().theme
        )
    }

    override fun displayBalance(balance: Int) {
        binding.balanceTextView.text = balance.toString()
    }

    override fun setCardCameraDistance() {
        val scale = requireActivity().applicationContext.resources.displayMetrics.density
        with(binding) {

            for (card in getCardList()) {
                card.cameraDistance = 8000 * scale
            }

            for (frontCard in getCardFrontList()) {
                frontCard.cameraDistance = 8000 * scale
            }
        }
    }

    override fun setOnClickListenerOnBackButton() {
        with(binding) {
            backButton.setOnClickListener {
                presenter.playOnClickSound()
                if (presenter.musicVolume == 0) {
                    (activity as MainActivity).vibrate(context)
                }
                findNavController().navigate(R.id.action_bonusGameFragment_to_chooseGameFragment)
            }
        }
    }

    override fun setOnClickListenerOnCards() {
        val cardItemList = getBonusCards().shuffled()

        for (i in (0..3)) {
            setOnClickListenerOnCard(
                getCardList()[i],
                getCardFrontList()[i],
                cardItemList[i],
                i
            )
        }
    }

    override fun createAnimators() {
        front_animation = AnimatorInflater.loadAnimator(
            requireActivity().applicationContext,
            R.animator.front_animator
        ) as AnimatorSet
        back_animation = AnimatorInflater.loadAnimator(
            requireActivity().applicationContext,
            R.animator.back_animator
        ) as AnimatorSet
    }
}