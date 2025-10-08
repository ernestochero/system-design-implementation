package mini_project_01.domain

sealed trait AssetState

object AssetState {
  case object Ready extends AssetState
  case object Failed extends AssetState
  case object Pending extends AssetState
}

case class Asset(id: String, platform: String, state: AssetState)
